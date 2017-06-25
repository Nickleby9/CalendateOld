package com.calendate.calendate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements SetButtonTitleDialog.OnTitleSetListener,
         NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_FIREBASE_SIGNIN = 2;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String buttonTitle;
    int fragNum = 0;
    TextView tvUsername;
    TextView tvEmail;


    FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if (firebaseAuth.getCurrentUser() == null) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                user = FirebaseAuth.getInstance().getCurrentUser();
                user.reload();
                String displayName = firebaseAuth.getCurrentUser().getDisplayName();
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fragNum = getIntent().getIntExtra("fragNum", 0) -1;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_buttons);
        navigationView.getMenu().performIdentifierAction(R.id.nav_buttons,0);

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        if (user != null) {
            tvUsername.setText(user.getDisplayName());
            tvEmail.setText(user.getEmail());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_buttons:
                Bundle bundle = new Bundle();
                bundle.putInt("fragNum", fragNum);
                ButtonsFragment buttonsFragment = new ButtonsFragment();
                buttonsFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, buttonsFragment).commit();
                break;
            case R.id.nav_calendar:

                break;
            case R.id.nav_timeline:

                break;
            case R.id.nav_sign_out:
                mAuth.signOut();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onTitleSet(String title, String btnRef, int fragNum) {
        ButtonsFragment.PlaceholderFragment p = new ButtonsFragment.PlaceholderFragment();
        p.setButtonText(btnRef, title, fragNum);
    }
}
