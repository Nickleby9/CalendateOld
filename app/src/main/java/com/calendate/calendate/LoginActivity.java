package com.calendate.calendate;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnHoverListener {

    Button btnLogin;
    TextView tvSignin;
    EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnSend);
        tvSignin = (TextView) findViewById(R.id.tvSignin);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        tvSignin.setOnHoverListener(this);

        btnLogin.setOnClickListener(this);
        tvSignin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btnSend:
                if (!etUsername.getText().toString().equals("no")){
                    Intent intent = new Intent(this,MainActivity.class);
                    //for firebase login:
//                    Intent intent = new Intent(this,EmailPasswordActivity.class);
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Details incorrect")
                            .setMessage("Unknown user name or bad password.")
                            .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Forgot password", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ForgotPassword f = new ForgotPassword();
                                    f.show(getSupportFragmentManager(), "forgotPassword");

//                                    Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
//                                    startActivity(intent);
                                }
                            });
                    AlertDialog dialog = builder.show();
                }

                break;
            case R.id.tvSignin:
                Intent intent1 = new Intent(this,RegistrationActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public boolean onHover(View v, MotionEvent event) {
        tvSignin.setTextColor(Color.BLUE);
        return false;
    }

}
