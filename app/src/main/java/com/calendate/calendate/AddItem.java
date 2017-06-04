package com.calendate.calendate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddItem extends AppCompatActivity implements View.OnClickListener {

    Spinner spnCount, spnKind;
    EditText etTitle, etDescription;
    Button btnDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        spnCount = (Spinner) findViewById(R.id.spnCount);
        spnKind = (Spinner) findViewById(R.id.spnKind);
        btnDate = (Button) findViewById(R.id.btnDate);
        btnDate.setOnClickListener(this);

        ArrayAdapter<CharSequence> spnKindAdapter = ArrayAdapter.createFromResource(this,R.array.kind,R.layout.activity_add_item);
        spnKindAdapter.setDropDownViewResource(R.layout.activity_add_item);
        spnKind.setAdapter(spnKindAdapter);


    }

    @Override
    public void onClick(View v) {
        DateDialogFragment date = new DateDialogFragment();
        if (btnDate.getText() != null) {
            date.show(getSupportFragmentManager(),"datePicker");
//            date.
        }
    }
}
