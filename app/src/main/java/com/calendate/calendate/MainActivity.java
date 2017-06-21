package com.calendate.calendate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.Scopes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ButtonsFragment.OnFragmentInteractionListener,
        SetButtonTitleDialog.OnTitleSetListener, ButtonsFragmentTwo.OnFragmentInteractionListener {

    private static final String BUTTON_ID = "btnId";
    private static final int RC_FIREBASE_SIGNIN = 2;
    TextView tvUser;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String buttonTitle;
    int currentFragment;

    FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if (firebaseAuth.getCurrentUser() == null) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
//            -----Auto Google UI Login-----
//            startActivityForResult(AuthUI.getInstance()
//                    .createSignInIntentBuilder()
//                    .setProviders(
//                            Arrays.asList(new AuthUI.IdpConfig.Builder(
//                                            AuthUI.EMAIL_PROVIDER).build(),
//                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
//                                            .setPermissions(Arrays.asList(Scopes.PROFILE, Scopes.EMAIL))
//                                            .build()))
//                    .build(), RC_FIREBASE_SIGNIN);
            } else {
                user = FirebaseAuth.getInstance().getCurrentUser();
                user.reload();
                String displayName = firebaseAuth.getCurrentUser().getDisplayName();
                tvUser.setText("Hello " + displayName);
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvUser = (TextView) findViewById(R.id.tvUser);

        if (user != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ButtonsFragment(), "frag_button_1").commit();
            currentFragment = 1;
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ButtonsFragmentTwo(), "frag_button_2").commit();
//            currentFragment = 2;
        }
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
            case R.id.action_delete:
                FirebaseAuth.getInstance().getCurrentUser().delete();
                return true;
            case R.id.action_sign_out:
                FirebaseAuth.getInstance().signOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onButtonPressed(int btnId, int buttonsNumber) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(BUTTON_ID, btnId);
        intent.putExtra("btnNum", buttonsNumber);
        startActivity(intent);
    }

    @Override
    public void onTitleSet(String title, int btnId) {
        if (currentFragment == 1) {
            Fragment setTitle = getSupportFragmentManager().findFragmentByTag("frag_button_1");
            if (setTitle != null) {
                ButtonsFragment bf = (ButtonsFragment) setTitle;
                BootstrapButton button = (BootstrapButton) findViewById(btnId);
                bf.setButtonText(button, title);
            }
        }
        if (currentFragment == 2) {
            Fragment setTitle = getSupportFragmentManager().findFragmentByTag("frag_button_2");
            if (setTitle != null) {
                ButtonsFragmentTwo bf = (ButtonsFragmentTwo) setTitle;
                BootstrapButton button = (BootstrapButton) findViewById(btnId);
                bf.setButtonText(button, title);
            }
        }

    }

}
