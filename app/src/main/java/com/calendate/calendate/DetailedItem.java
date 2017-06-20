package com.calendate.calendate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DetailedItem extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    Spinner spnCount, spnKind, spnRepeat;
    EditText etTitle, etDescription;
    BootstrapButton btnDate, btnChange, btnTime;
    Calendar c;
    FirebaseDatabase mDatabase;
    FirebaseUser user;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_item);

//        etTitle.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        etTitle = (EditText) findViewById(R.id.etTitle);
        etDescription = (EditText) findViewById(R.id.etDescription);
        btnDate = (BootstrapButton) findViewById(R.id.btnDate);
        spnCount = (Spinner) findViewById(R.id.spnCount);
        spnKind = (Spinner) findViewById(R.id.spnKind);
        btnTime = (BootstrapButton) findViewById(R.id.btnTime);
        spnRepeat = (Spinner) findViewById(R.id.spnRepeat);
        btnChange = (BootstrapButton) findViewById(R.id.btnChange);

        MyUtils.fixBootstrapButton(this, btnDate);
        MyUtils.fixBootstrapButton(this, btnTime);
        MyUtils.fixBootstrapButton(this, btnChange);

        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnChange.setOnClickListener(this);

        changeEnabled(false);

        key = getIntent().getStringExtra("key");

        String fixEmail = MyUtils.fixEmail(user.getEmail());
        mDatabase.getReference("events/" + fixEmail).child(key);

        readOnce();
    }

    private void readOnce() {
        String fixEmail = MyUtils.fixEmail(user.getEmail());
        mDatabase.getReference("events/" + fixEmail + "/" + key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(Event.class);
                etTitle.setText(event.getTitle());
                etDescription.setText(event.getDescription());
                btnDate.setText(event.getC());
//                    spnCount.(event.getTitle());
//                    spnKind
                btnTime.setText(event.getTime());
//                spnRepeat
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    //TODO:fix that^
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnChange:
                if (btnChange.getText().equals(getString(R.string.btn_edit))) {
                    changeEnabled(true);
                    btnChange.setText(R.string.btn_save);
                } else if (btnChange.getText().equals(getString(R.string.btn_save))) {
                    changeEnabled(false);
                    btnChange.setText(R.string.btn_edit);

                    Intent intent = new Intent(DetailedItem.this, DetailActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btnDate:
                if (btnDate.isClickable()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
                    try {
                        c.setTime(dateFormat.parse(btnDate.getText().toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DatePickerDialog pickerDialog = new DatePickerDialog(v.getContext());
                    pickerDialog = new DatePickerDialog(v.getContext(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

                    pickerDialog.show();
                }
                break;
            case R.id.btnTime:
                if (btnTime.isClickable()) {
//                TimePickerDialog timeDialog = new TimePickerDialog(v.getContext(), this, hours, minutes, true);
//                timeDialog.show();
                }
                break;
        }
    }

    void changeEnabled(Boolean state) {
        etDescription.setEnabled(state);
        etTitle.setEnabled(state);
        spnCount.setEnabled(state);
        spnKind.setEnabled(state);
        btnTime.setClickable(state);
        spnRepeat.setEnabled(state);
        btnDate.setClickable(state);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
