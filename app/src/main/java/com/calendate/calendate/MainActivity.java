package com.calendate.calendate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.Scopes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SetButtonTitleDialog.OnTitleSetListener {

    private static final int RC_FIREBASE_SIGNIN = 2;
    TextView tvUser;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String buttonTitle;
    int fragNum;


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

    ViewPager viewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;

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
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mSectionsPagerAdapter);
        int fragNumToGo = getIntent().getIntExtra("fragNum", 0) -1;
        viewPager.setCurrentItem(fragNumToGo);

        if (user != null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ButtonsFragment(), "frag_button_1").commit();
//            currentFragment = 1;
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
    public void onTitleSet(String title, String btnRef, int fragNum) {
        PlaceholderFragment p = new PlaceholderFragment();
        p.setButtonText(btnRef, title, fragNum);
    }

//    @Override
//    public void onButtonPressed(int btnId, int fragNum) {
//
//    }


    public static class PlaceholderFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        int fragNum = 0;
        BootstrapButton btnTopLeft;
        BootstrapButton btnTopRight;
        BootstrapButton btnMiddleLeft;
        BootstrapButton btnMiddleRight;
        BootstrapButton btnBottomLeft;
        BootstrapButton btnBottomRight;
        String btnRef = "";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_buttons, container, false);
            fragNum = getArguments().getInt(ARG_SECTION_NUMBER);

            btnTopLeft = (BootstrapButton) rootView.findViewById(R.id.btnTopLeft);
            btnTopRight = (BootstrapButton) rootView.findViewById(R.id.btnTopRight);
            btnMiddleLeft = (BootstrapButton) rootView.findViewById(R.id.btnMiddleLeft);
            btnMiddleRight = (BootstrapButton) rootView.findViewById(R.id.btnMiddleRight);
            btnBottomLeft = (BootstrapButton) rootView.findViewById(R.id.btnBottomLeft);
            btnBottomRight = (BootstrapButton) rootView.findViewById(R.id.btnBottomRight);

            btnTopLeft.setBootstrapBrand(new CustomBootstrapStyleTransparent(rootView.getContext()));
            btnTopRight.setBootstrapBrand(new CustomBootstrapStyleTransparent(rootView.getContext()));
            btnMiddleLeft.setBootstrapBrand(new CustomBootstrapStyleTransparent(rootView.getContext()));
            btnMiddleRight.setBootstrapBrand(new CustomBootstrapStyleTransparent(rootView.getContext()));
            btnBottomLeft.setBootstrapBrand(new CustomBootstrapStyleTransparent(rootView.getContext()));
            btnBottomRight.setBootstrapBrand(new CustomBootstrapStyleTransparent(rootView.getContext()));

            mDatabase.getReference("buttons/" + user.getUid() + "/" + fragNum + "/" + "topLeft")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            btnTopLeft.setText(dataSnapshot.getValue(String.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            mDatabase.getReference("buttons/" + user.getUid() + "/" + fragNum + "/" + "topRight")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            btnTopRight.setText(dataSnapshot.getValue(String.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            mDatabase.getReference("buttons/" + user.getUid() + "/" + fragNum + "/" + "middleLeft")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            btnMiddleLeft.setText(dataSnapshot.getValue(String.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            mDatabase.getReference("buttons/" + user.getUid() + "/" + fragNum + "/" + "middleRight")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            btnMiddleRight.setText(dataSnapshot.getValue(String.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            mDatabase.getReference("buttons/" + user.getUid() + "/" + fragNum + "/" + "bottomLeft")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            btnBottomLeft.setText(dataSnapshot.getValue(String.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            mDatabase.getReference("buttons/" + user.getUid() + "/" + fragNum + "/" + "bottomRight")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            btnBottomRight.setText(dataSnapshot.getValue(String.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            btnTopLeft.setOnClickListener(this);
            btnTopRight.setOnClickListener(this);
            btnMiddleLeft.setOnClickListener(this);
            btnMiddleRight.setOnClickListener(this);
            btnBottomLeft.setOnClickListener(this);
            btnBottomRight.setOnClickListener(this);

            btnTopLeft.setOnLongClickListener(this);
            btnTopRight.setOnLongClickListener(this);
            btnMiddleLeft.setOnLongClickListener(this);
            btnMiddleRight.setOnLongClickListener(this);
            btnBottomLeft.setOnLongClickListener(this);
            btnBottomRight.setOnLongClickListener(this);
            return rootView;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.btnTopLeft:
                    btnRef = "topLeft";
                    break;
                case R.id.btnTopRight:
                    btnRef = "topRight";
                    break;
                case R.id.btnMiddleLeft:
                    btnRef = "middleLeft";
                    break;
                case R.id.btnMiddleRight:
                    btnRef = "middleRight";
                    break;
                case R.id.btnBottomLeft:
                    btnRef = "bottomLeft";
                    break;
                case R.id.btnBottomRight:
                    btnRef = "bottomRight";
                    break;
            }
            onButtonPressed(btnRef, fragNum);
        }

        @Override
        public boolean onLongClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.btnTopLeft:
                    btnRef = "topLeft";
                    break;
                case R.id.btnTopRight:
                    btnRef = "topRight";
                    break;
                case R.id.btnMiddleLeft:
                    btnRef = "middleLeft";
                    break;
                case R.id.btnMiddleRight:
                    btnRef = "middleRight";
                    break;
                case R.id.btnBottomLeft:
                    btnRef = "bottomLeft";
                    break;
                case R.id.btnBottomRight:
                    btnRef = "bottomRight";
                    break;
            }
            showButtonOptionsDialog(v, btnRef, fragNum);
            return false;
        }

        public void showButtonOptionsDialog(final View v, final String btnRef, final int fragNum) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(R.string.change_button_dialog_title);
            builder.setItems(R.array.buttonOptions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            //Change title
                            SetButtonTitleDialog f = new SetButtonTitleDialog();
                            Bundle args = new Bundle();
                            args.putString("btnRef", btnRef);
                            args.putInt("fragNum", fragNum);
                            f.setArguments(args);
                            f.show(getFragmentManager(), "setButtonTitleDialog");
                            break;
                        case 1:
                            //Change image

                            break;
                        case 2:
                            //Delete - are you sure? -remove title and link to data
                            setButtonText(btnRef, "", fragNum);
                            break;
                    }
                }
            });
            builder.show();
        }

        public void setButtonText(String btnRef, String text, int fragNum) {
            mDatabase.getReference("buttons/" + user.getUid() + "/" + fragNum + "/" + btnRef).setValue(text);
        }

        public void onButtonPressed(String btnRef, int fragNum) {

            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra("btnRef", btnRef);
            intent.putExtra("fragNum", fragNum);
            startActivity(intent);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "1";
                case 1:
                    return "2";
            }
            return null;
        }
    }
}
