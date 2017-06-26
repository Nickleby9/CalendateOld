package com.calendate.calendate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.calendate.calendate.models.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.LocalDateTime;

public class AddItem extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Spinner spnKind, spnRepeat;
    EditText etTitle, etDescription, etCount;
    BootstrapButton btnDate, btnSave, btnClear, btnTime;
    LocalDateTime date = new LocalDateTime(LocalDateTime.now());
    int hours = 0, minutes = 0;
    FirebaseDatabase mDatabase;
    FirebaseUser user;
    String btnRef;
    int buttonsNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        btnRef = getIntent().getStringExtra("btnRef");
        buttonsNumber = getIntent().getIntExtra("fragNum", 0);

        etTitle = (EditText) findViewById(R.id.etTitle);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etCount = (EditText) findViewById(R.id.etCount);
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

        ArrayAdapter<CharSequence> spnKindAdapter = ArrayAdapter.createFromResource(this, R.array.kind, R.layout.spinner_item);
        spnKindAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnKind.setAdapter(spnKindAdapter);

        ArrayAdapter<CharSequence> spnRepeatAdapter = ArrayAdapter.createFromResource(this, R.array.repeat, R.layout.spinner_item);
        spnKindAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnRepeat.setAdapter(spnRepeatAdapter);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnDate:
                DatePickerDialog pickerDialog = new DatePickerDialog(v.getContext(), this, date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
                pickerDialog.show();
                break;
            case R.id.btnTime:
                TimePickerDialog timeDialog = new TimePickerDialog(v.getContext(), this, hours, minutes, true);
                timeDialog.show();
                break;
            case R.id.btnSave:
                if (isEmptyFields()){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle(getString(R.string.error));
                    dialog.setMessage(getString(R.string.error_empty_fields));
                    dialog.setNeutralButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                } else {
                    addNewEvent();
                }
                break;
            case R.id.btnClear:
                etTitle.setText("");
                etDescription.setText("");
                btnDate.setText(R.string.add_item_pick_a_date);

                break;
        }
    }

    private boolean isEmptyFields() {
        int alertCount = -1, alertKind = -1, repeat = -1;
        String title = etTitle.getText().toString();
        String description = etTitle.getText().toString();
        alertCount = Integer.valueOf(etCount.getText().toString());
        alertKind = spnKind.getSelectedItemPosition();
        String time = btnTime.getText().toString();
        repeat = spnRepeat.getSelectedItemPosition();
        if (title.isEmpty() || alertCount == -1 || alertKind == -1 || time.equals(getString(R.string.btn_set_time))
                || repeat == -1 || btnDate.getText().toString().equals(getString(R.string.add_item_pick_a_date))) {
            return true;
        } else {
            return false;
        }
    }

    private void addNewEvent() {
        String title = etTitle.getText().toString();
        final String description = etDescription.getText().toString();
        int alertCount = Integer.valueOf(etCount.getText().toString());
        int alertKind = spnKind.getSelectedItemPosition();
        String time = btnTime.getText().toString();
        int repeat = spnRepeat.getSelectedItemPosition();

        String key = mDatabase.getReference("events/" + user.getUid() + "/" + btnRef + buttonsNumber).push().getKey();
        Event event = new Event(title, description, date, alertCount, alertKind, hours, minutes, repeat, key);
        mDatabase.getReference("events/" + user.getUid() + "/" + btnRef + buttonsNumber).child(key).setValue(event);

        Intent intent = new Intent(AddItem.this, DetailActivity.class);
        intent.putExtra("btnRef", btnRef);
        intent.putExtra("fragNum", buttonsNumber);
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
