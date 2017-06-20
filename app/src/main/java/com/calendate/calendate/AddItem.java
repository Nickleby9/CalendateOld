package com.calendate.calendate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.LocalDateTime;

public class AddItem extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Spinner spnCount, spnKind, spnRepeat;
    EditText etTitle, etDescription;
    BootstrapButton btnDate, btnSave, btnClear, btnTime;
    LocalDateTime date = new LocalDateTime(LocalDateTime.now());
    int hours = 0, minutes = 0;
    FirebaseDatabase mDatabase;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        etTitle = (EditText) findViewById(R.id.etTitle);
        etDescription = (EditText) findViewById(R.id.etDescription);
        spnCount = (Spinner) findViewById(R.id.spnCount);
        spnKind = (Spinner) findViewById(R.id.spnKind);
        spnRepeat = (Spinner) findViewById(R.id.spnRepeat);
        btnTime = (BootstrapButton) findViewById(R.id.btnTime);
        btnDate = (BootstrapButton) findViewById(R.id.btnDate);
        btnSave = (BootstrapButton) findViewById(R.id.btnSave);
        btnClear = (BootstrapButton) findViewById(R.id.btnClear);

        etTitle.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        MyUtils.fixBootstrapButton(this, btnDate);
        MyUtils.fixBootstrapButton(this, btnTime);
        MyUtils.fixBootstrapButton(this, btnSave);
        MyUtils.fixBootstrapButton(this, btnClear);

        ArrayAdapter<CharSequence> spnCountAdapter = ArrayAdapter.createFromResource(this, R.array.count, R.layout.spinner_item);
        spnCountAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnCount.setAdapter(spnCountAdapter);

        ArrayAdapter<CharSequence> spnKindAdapter = ArrayAdapter.createFromResource(this, R.array.kind, R.layout.spinner_item);
        spnKindAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnKind.setAdapter(spnKindAdapter);

        ArrayAdapter<CharSequence> spnRepeatAdapter = ArrayAdapter.createFromResource(this, R.array.repeat, R.layout.spinner_item);
        spnKindAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnRepeat.setAdapter(spnRepeatAdapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    //TODO:fix that ^
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnDate:
                DatePickerDialog pickerDialog = new DatePickerDialog(v.getContext());
                pickerDialog = new DatePickerDialog(v.getContext(), this, date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
                pickerDialog.show();
                break;
            case R.id.btnTime:
                TimePickerDialog timeDialog = new TimePickerDialog(v.getContext(), this, hours, minutes, true);
                timeDialog.show();
                break;
            case R.id.btnSave:
                addNewEvent();
                break;
            case R.id.btnClear:
                etTitle.setText("");
                etDescription.setText("");
                btnDate.setText(R.string.add_item_pick_a_date);

                break;
        }
    }

    private void addNewEvent() {
        String title = etTitle.getText().toString();
        final String description = etTitle.getText().toString();
        int alertCount = spnCount.getSelectedItemPosition();
        int alertKind = spnKind.getSelectedItemPosition();
        String time = btnTime.getText().toString();
        int repeat = spnRepeat.getSelectedItemPosition();

        String key = mDatabase.getReference("events/" + user.getUid()).push().getKey();
        Event event = new Event(title, description, date, alertCount, alertKind, hours, minutes, repeat, key);
        mDatabase.getReference("events/" + user.getUid() + "/" + key).setValue(event);

        Intent intent = new Intent(AddItem.this, DetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = new LocalDateTime(year, month, dayOfMonth, 0, 0);
        btnDate.setText(date.toString("MMMM d, yyyy"));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hours = hourOfDay;
        minutes = minute;
        btnTime.setText(String.valueOf(hours) + ":" + String.valueOf(minutes));
    }
}
