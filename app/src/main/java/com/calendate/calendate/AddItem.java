package com.calendate.calendate;

import android.app.DatePickerDialog;
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

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddItem extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    Spinner spnCount, spnKind, spnRepeat;
    EditText etTitle, etDescription;
    BootstrapButton btnDate, btnSave, btnClear;
    Date date;
    Calendar c;
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
        btnDate = (BootstrapButton) findViewById(R.id.btnDate);
        btnSave = (BootstrapButton) findViewById(R.id.btnSave);
        btnClear = (BootstrapButton) findViewById(R.id.btnClear);

        etTitle.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        btnDate.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        btnDate.setBootstrapBrand(new CustomBootstrapStyle(this));
        btnSave.setBootstrapBrand(new CustomBootstrapStyle(this));
        btnClear.setBootstrapBrand(new CustomBootstrapStyle(this));


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
                String btnText = btnDate.getText().toString();
                if (btnText.equals(getResources().getString(R.string.add_item_pick_a_date))) {
                    c = Calendar.getInstance();
                    pickerDialog = new DatePickerDialog(v.getContext(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                } else {
                    pickerDialog = new DatePickerDialog(v.getContext(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                }
                pickerDialog.show();
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
        String description = etTitle.getText().toString();
        int alertCount = Integer.parseInt(spnCount.getSelectedItem().toString());
        String alertKind = spnKind.getSelectedItem().toString();
        String repeat = spnRepeat.getSelectedItem().toString();

        String fixEmail = MyUtils.fixEmail(user.getEmail());
        String key = mDatabase.getReference("events/" + fixEmail).push().getKey();
        Event event = new Event(title, description, c, alertCount, alertKind, repeat, key);
        mDatabase.getReference("events/" + fixEmail).child(key).setValue(event);
        Intent intent = new Intent(AddItem.this, DetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        c.set(year, month, dayOfMonth);
        btnDate.setText(dateFormat.format(new Date(c.getTimeInMillis())));
    }


}
