package com.calendate.calendate;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import static com.beardedhen.androidbootstrap.font.FontAwesome.FA_SIGN_IN;
import static com.beardedhen.androidbootstrap.font.FontAwesome.FA_USER_PLUS;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_GOOGLE_LOGIN = 1;
    BootstrapButton btnLogin, btnRegister;
    Button btnGoogle;
    EditText etUsername, etPassword;
    FirebaseAuth mAuth;
    Boolean exit = false;
    GoogleApiClient mApiClient;
    FirebaseDatabase mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDatabase = FirebaseDatabase.getInstance();

        btnLogin = (BootstrapButton) findViewById(R.id.btnLogin);
        btnRegister = (BootstrapButton) findViewById(R.id.btnRegister);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnGoogle = (Button) findViewById(R.id.btnGoogle);

        btnLogin.setBootstrapBrand(new CustomBootstrapStyle(this));
        btnRegister.setBootstrapBrand(new CustomBootstrapStyle(this));

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setBootstrapText(new BootstrapText.Builder(this)
                .addText(getString(R.string.btn_login) + " ")
                .addFontAwesomeIcon(FA_SIGN_IN)
                .build()
        );

        btnRegister.setBootstrapText(new BootstrapText.Builder(this)
                .addText(getString(R.string.btn_register) + " ")
                .addFontAwesomeIcon(FA_USER_PLUS)
                .build()
        );

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btnLogin:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (username.contains("@")) {
                    if (!username.equals("") && !password.equals("")) {
                        showProgress(true);
                        mAuth.signInWithEmailAndPassword(username, password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        showProgress(false);
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if (user != null) {
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showProgress(false);
                                detailsIncorrect();
                            }
                        });
                    } else {
                        detailsIncorrect();
                    }
                } else {
//                    mDatabase.getReference("users").

                }
                break;
            case R.id.btnRegister:
                Intent intent1 = new Intent(this, RegistrationActivity.class);
                startActivity(intent1);
                break;
            case R.id.btnGoogle:
                showProgress(true);
                Intent googleIntent = Auth.GoogleSignInApi
                        .getSignInIntent(mApiClient);
                startActivityForResult(googleIntent, RC_GOOGLE_LOGIN);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_LOGIN) {
            GoogleSignInResult result = Auth.GoogleSignInApi
                    .getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                AuthCredential credential = GoogleAuthProvider
                        .getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                showProgress(false);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showProgress(false);
                        Log.d("Ness", e.toString());
                        Toast.makeText(LoginActivity.this,e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Permissions has not been granted", Toast.LENGTH_SHORT).show();
            }
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
                        showProgress(false);
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
        if (!exit) {
            Toast.makeText(this, R.string.back_twice, Toast.LENGTH_SHORT).show();
            exit = true;
        } else {
            ActivityCompat.finishAffinity(this);
        }
    }

    private ProgressDialog dialog;

    private void showProgress(boolean show) {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setTitle(getString(R.string.logging));
            dialog.setMessage(etUsername.getText());
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }
        if (show)
            dialog.show();
        else
            dialog.dismiss();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed to connect to Google API services", Toast.LENGTH_SHORT).show();
    }
}
