package com.calendate.calendate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements ButtonsFragment.OnFragmentInteractionListener, SetButtonTitleDialog.OnTitleSetListener, FirebaseAuth.AuthStateListener {

    private static final String BUTTON_ID = "btnId";
    TextView tvUser;
    String buttonTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO:extract all authentication methond to a new class
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvUser = (TextView) findViewById(R.id.tvUser);

        FirebaseAuth.getInstance().addAuthStateListener(this);


        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ButtonsFragment(),"fragment_TAG").commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_sign_out:
                FirebaseAuth.getInstance().signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    //TODO:
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            String displayName = firebaseAuth.getCurrentUser().getDisplayName();
            tvUser.setText("Hello " + displayName);
        }
    }

    @Override
    public void onFragmentInteraction(int btnId) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(BUTTON_ID, btnId);
        startActivity(intent);
    }


    @Override
    public void onTitleSet(String title, int btnId) {
        Fragment setTitle = getSupportFragmentManager().findFragmentByTag("fragment_TAG");
        if (setTitle != null){
            ButtonsFragment bf = (ButtonsFragment) setTitle;
            Button button = (Button) findViewById(btnId);
            bf.setButtonText(button, title);
        }
    }

}
