package com.calendate.calendate;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    TextView tvSignin;
    EditText etUsername, etPassword;
    FirebaseAuth mAuth;
    Boolean exit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnSend);
        tvSignin = (TextView) findViewById(R.id.tvSignin);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnLogin.setOnClickListener(this);
        tvSignin.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btnSend:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (!username.equals("") && !password.equals("")) {
                    mAuth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user != null) {
                                        String displayName = user.getDisplayName();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("user", displayName);
                                        startActivity(intent);
                                    }
                                    if (!task.isSuccessful()) {
                                        detailsIncorrect();
                                    }
                                }
                            });


                } else {
                    detailsIncorrect();
                }
                break;
            case R.id.tvSignin:
                Intent intent1 = new Intent(this, RegistrationActivity.class);
                startActivity(intent1);
                break;
        }
    }

    private void detailsIncorrect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.login_error_title)
                .setMessage(R.string.login_error_description)
                .setPositiveButton(R.string.login_error_try, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.login_error_forgot, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ForgotPassword f = new ForgotPassword();
                        f.show(getSupportFragmentManager(), "forgotPassword");
                    }
                });
        AlertDialog dialog = builder.show();
    }

    @Override
    public void onBackPressed() {
        if (exit) {
//            System.exit(0);
            ActivityCompat.finishAffinity(this);
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            exit = true;
        }
    }
}
