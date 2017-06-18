package com.calendate.calendate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class DetailedItem extends AppCompatActivity {

    Spinner spnCount, spnKind, spnRepeat;
    EditText etTitle, etDescription;
    BootstrapButton btnDate, btnChange;
    Calendar c;
    FirebaseDatabase mDatabase;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_item);

        mDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        etTitle = (EditText) findViewById(R.id.etTitle);
        etDescription = (EditText) findViewById(R.id.etDescription);
        spnCount = (Spinner) findViewById(R.id.spnCount);
        spnKind = (Spinner) findViewById(R.id.spnKind);
        spnRepeat = (Spinner) findViewById(R.id.spnRepeat);
        btnDate = (BootstrapButton) findViewById(R.id.btnDate);
        btnChange = (BootstrapButton) findViewById(R.id.btnChange);

        btnDate.setBootstrapBrand(new CustomBootstrapStyle(this));
        btnChange.setBootstrapBrand(new CustomBootstrapStyle(this));


    }
}
