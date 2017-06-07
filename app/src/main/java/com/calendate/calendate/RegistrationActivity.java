package com.calendate.calendate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRegister;
    EditText etUsername, etPassword, etPassword2, etEmail;
    private FirebaseAuth mAuth;
    TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPassword2 = (EditText) findViewById(R.id.etPassword2);
        etEmail = (EditText) findViewById(R.id.etEmail);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        tvError = (TextView) findViewById(R.id.tvError);

        btnRegister.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        tvError.setVisibility(View.INVISIBLE);
        final String username = etUsername.getText().toString();
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        String password2 = etPassword2.getText().toString();
        if (!username.equals("") && !email.equals("") && !password.equals("") && !password2.equals("")) {
            if (password.equals(password2)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                mAuth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(Task<AuthResult> task) {
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                UserProfileChangeRequest change = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                                                if (user != null) {
                                                    user.updateProfile(change);
                                                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                                    intent.putExtra("user", username);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                if (!task.isSuccessful()) {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException e) {
                                        etPassword.setError(getString(R.string.error_weak_password));
                                        etPassword.requestFocus();
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        etEmail.setError(getString(R.string.error_invalid_email));
                                        etEmail.requestFocus();
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        etEmail.setError(getString(R.string.error_user_exists));
                                        etEmail.requestFocus();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
            } else {
                etPassword2.setError(getString(R.string.error_mismatch_passwords));
                etPassword2.requestFocus();
            }
        } else {
            tvError.setText(R.string.error_empty_fields);
            tvError.setVisibility(View.VISIBLE);
        }
    }
}

