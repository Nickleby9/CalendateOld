package com.calendate.calendate;

import android.app.DatePickerDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddItem extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    Spinner spnCount, spnKind;
    EditText etTitle, etDescription;
    Button btnDate;
    Date date;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        spnCount = (Spinner) findViewById(R.id.spnCount);
        spnKind = (Spinner) findViewById(R.id.spnKind);
        btnDate = (Button) findViewById(R.id.btnDate);
        btnDate.setOnClickListener(this);

        ArrayAdapter<CharSequence> spnCountAdapter = ArrayAdapter.createFromResource(this, R.array.count, R.layout.spinner_item);
        spnCountAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnCount.setAdapter(spnCountAdapter);

        ArrayAdapter<CharSequence> spnKindAdapter = ArrayAdapter.createFromResource(this, R.array.kind, R.layout.spinner_item);
        spnKindAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnKind.setAdapter(spnKindAdapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        DatePickerDialog pickerDialog = new DatePickerDialog(v.getContext());
        String btnText = btnDate.getText().toString();

        if (btnText.equals("Pick a Date")) {
            c = Calendar.getInstance();
            pickerDialog = new DatePickerDialog(v.getContext(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        } else {
            pickerDialog = new DatePickerDialog(v.getContext(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        }
        pickerDialog.show();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        c.set(year, month, dayOfMonth);
        btnDate.setText(dateFormat.format(new Date(c.getTimeInMillis())));

    }

}
