package com.calendate.calendate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    BootstrapButton btnLogin;
    TextView tvSignin;
    EditText etUsername, etPassword;
    FirebaseAuth mAuth;
    Boolean exit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (BootstrapButton) findViewById(R.id.btnLogin);
        tvSignin = (TextView) findViewById(R.id.tvSignin);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnLogin.setOnClickListener(this);
        tvSignin.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

//        BootstrapText text = new BootstrapText.Builder(this).addFontAwesomeIcon("{fa-facebook").build();
//        btnLogin.setBootstrapText(text);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btnLogin:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (!username.equals("") && !password.equals("")) {
                    showProgress(true);
                    mAuth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user != null) {
                                        showProgress(false);
                                        String displayName = user.getDisplayName();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
            //TODO:not dismissable
            dialog = new ProgressDialog(this);
            dialog.setMessage("Logging in...");
            dialog.setTitle("Connecting to server");
        }
        if (show)
            dialog.show();
        else
            dialog.dismiss();
    }
}
