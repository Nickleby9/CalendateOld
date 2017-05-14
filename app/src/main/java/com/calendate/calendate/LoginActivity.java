package com.calendate.calendate;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnHoverListener {

    Button btnLogin;
    TextView tvSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvSignin = (TextView) findViewById(R.id.tvSignin);
        tvSignin.setOnHoverListener(this);

        btnLogin.setOnClickListener(this);
        tvSignin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btnLogin:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.tvSignin:

                break;
        }
    }

    @Override
    public boolean onHover(View v, MotionEvent event) {
        tvSignin.setTextColor(Color.BLUE);
        return false;
    }
}
