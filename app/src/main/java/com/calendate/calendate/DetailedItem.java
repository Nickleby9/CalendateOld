package com.calendate.calendate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

public class DetailedItem extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Spinner spnCount, spnKind, spnRepeat;
    EditText etTitle, etDescription;
    BootstrapButton btnDate, btnChange, btnTime;
    LocalDateTime date;
    FirebaseDatabase mDatabase;
    FirebaseUser user;
    String key;
    int hours = 0, minutes = 0;
    String btnRef;
    int fragNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_item);

//        etTitle.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        btnRef = getIntent().getStringExtra("btnRef");
        fragNum = getIntent().getIntExtra("fragNum", 0);

        etTitle = (EditText) findViewById(R.id.etTitle);
        etDescription = (EditText) findViewById(R.id.etDescription);
        btnDate = (BootstrapButton) findViewById(R.id.btnDate);
        spnCount = (Spinner) findViewById(R.id.spnCount);
        spnKind = (Spinner) findViewById(R.id.spnKind);
        btnTime = (BootstrapButton) findViewById(R.id.btnTime);
        spnRepeat = (Spinner) findViewById(R.id.spnRepeat);
        btnChange = (BootstrapButton) findViewById(R.id.btnChange);

        key = getIntent().getStringExtra("key");

        MyUtils.fixBootstrapButton(this, btnDate);
        MyUtils.fixBootstrapButton(this, btnTime);
        MyUtils.fixBootstrapButton(this, btnChange);

        ArrayAdapter<CharSequence> spnCountAdapter = ArrayAdapter.createFromResource(this, R.array.count, R.layout.spinner_item);
        spnCountAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnCount.setAdapter(spnCountAdapter);

        ArrayAdapter<CharSequence> spnKindAdapter = ArrayAdapter.createFromResource(this, R.array.kind, R.layout.spinner_item);
        spnKindAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnKind.setAdapter(spnKindAdapter);

        ArrayAdapter<CharSequence> spnRepeatAdapter = ArrayAdapter.createFromResource(this, R.array.repeat, R.layout.spinner_item);
        spnKindAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnRepeat.setAdapter(spnRepeatAdapter);

        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnChange.setOnClickListener(this);

        changeEnabled(false);

        mDatabase.getReference("events/" + user.getUid() + "/" + btnRef);

        readOnce();
    }

    private void readOnce() {
        mDatabase.getReference("events/" + user.getUid() + "/" + btnRef + fragNum).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(key)) {
                        Event event = snapshot.getValue(Event.class);
                        etTitle.setText(event.getTitle());
                        etDescription.setText(event.getDescription());
                        btnDate.setText(event.getDate());
                        spnCount.setSelection(event.getAlertCountPos());
                        spnKind.setSelection(event.getAlertKindPos());
                        btnTime.setText(event.getTime());
                        spnRepeat.setSelection(event.getRepeatPos());
                        key = snapshot.getKey();
                        date = new LocalDateTime(LocalDateTime.parse(btnDate.getText().toString(), DateTimeFormat.forPattern("MMMM d, yyyy")));
                        String[] split = btnTime.getText().toString().split(":");
                        hours = Integer.valueOf(split[0]);
                        minutes = Integer.valueOf(split[1]);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

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

                    String title = etTitle.getText().toString();
                    String description = etDescription.getText().toString();
                    int alertCount = Integer.parseInt(spnCount.getSelectedItem().toString());
                    int alertKind = spnKind.getSelectedItemPosition();
                    int repeat = spnRepeat.getSelectedItemPosition();

                    Event event = new Event(title, description, date, alertCount, alertKind, hours, minutes, repeat, key);
                    mDatabase.getReference("events/" + user.getUid() + "/" + btnRef + fragNum + "/" + key).setValue(event);

                    Intent intent = new Intent(DetailedItem.this, DetailActivity.class);
                    intent.putExtra("btnRef", btnRef);
                    intent.putExtra("fragNum", fragNum);
                    startActivity(intent);
                }
                break;
            case R.id.btnDate:
                if (btnDate.isClickable()) {
                    DatePickerDialog pickerDialog = new DatePickerDialog(v.getContext(), this, date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
                    pickerDialog.show();
                }
                break;
            case R.id.btnTime:
                if (btnTime.isClickable()) {
                    TimePickerDialog timeDialog = new TimePickerDialog(v.getContext(), this, hours, minutes, true);
                    timeDialog.show();
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
        date = new LocalDateTime(year, month, dayOfMonth, 0, 0);
        btnDate.setText(date.toString("MMMM d, yyyy"));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        hours = hourOfDay;
        minutes = minute;
        btnTime.setText(String.valueOf(hours) + ":" + String.valueOf(minutes));
    }
}
