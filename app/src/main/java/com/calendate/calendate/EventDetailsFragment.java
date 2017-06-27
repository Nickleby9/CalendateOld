package com.calendate.calendate;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.calendate.calendate.models.Event;
import com.calendate.calendate.models.EventRow;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailsFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String ARG_BTNREF = "btnRef";
    private static final String ARG_FRAGNUM = "fragNum";
    private static final String ARG_MODEL = "model";
    Spinner spnKind, spnRepeat;
    EditText etTitle, etDescription, etCount;
    BootstrapButton btnDate, btnChange, btnTime;
    LocalDateTime date;
    FirebaseDatabase mDatabase;
    FirebaseUser user;
    String key = "";
    int hours = 0, minutes = 0;
    String btnRef;
    int fragNum;
    EventRow model;

    public EventDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        model = getArguments().getParcelable(ARG_MODEL);

        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etDescription = (EditText) view.findViewById(R.id.etDescription);
        btnDate = (BootstrapButton) view.findViewById(R.id.btnDate);
        etCount = (EditText) view.findViewById(R.id.etCount);
        spnKind = (Spinner) view.findViewById(R.id.spnKind);
        btnTime = (BootstrapButton) view.findViewById(R.id.btnTime);
        spnRepeat = (Spinner) view.findViewById(R.id.spnRepeat);
        btnChange = (BootstrapButton) view.findViewById(R.id.btnChange);

        MyUtils.fixBootstrapButton(getContext(), btnDate);
        MyUtils.fixBootstrapButton(getContext(), btnTime);
        MyUtils.fixBootstrapButton(getContext(), btnChange);

        ArrayAdapter<CharSequence> spnKindAdapter = ArrayAdapter.createFromResource(getContext(), R.array.kind, R.layout.spinner_item);
        spnKindAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnKind.setAdapter(spnKindAdapter);

        ArrayAdapter<CharSequence> spnRepeatAdapter = ArrayAdapter.createFromResource(getContext(), R.array.repeat, R.layout.spinner_item);
        spnKindAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnRepeat.setAdapter(spnRepeatAdapter);

        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnChange.setOnClickListener(this);

        changeEnabled(false);

        readOnce();

    }

    private void readOnce() {
        DatabaseReference ref = mDatabase.getReference();
        ref = mDatabase.getReference("all_events/" + user.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(model.getEventUID())) {
                        Event event = snapshot.getValue(Event.class);
                        etTitle.setText(event.getTitle());
                        etDescription.setText(event.getDescription());
                        btnDate.setText(event.getDate());
                        etCount.setText(String.valueOf(event.getAlertCount()));
                        spnKind.setSelection(event.getAlertKindPos());
                        btnTime.setText(event.getTime());
                        spnRepeat.setSelection(event.getRepeatPos());
                        key = snapshot.getKey();
                        date = new LocalDateTime(LocalDateTime.parse(btnDate.getText().toString(), DateTimeFormat.forPattern(MyUtils.dateFormat)));
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
                    int alertCount = Integer.parseInt(etCount.getText().toString());
                    int alertKind = spnKind.getSelectedItemPosition();
                    int repeat = spnRepeat.getSelectedItemPosition();

                    Event event = new Event(title, description, date, alertCount, alertKind, hours, minutes, repeat, key);
                    mDatabase.getReference("events/" + user.getUid() + "/" + btnRef + fragNum + "/" + key).setValue(event);

                    EventsFragment eventsFragment = new EventsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(ARG_BTNREF, btnRef);
                    bundle.putInt(ARG_FRAGNUM, fragNum);
                    eventsFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.frame, eventsFragment).commit();

//                    Intent intent = new Intent(DetailedItem.this, DetailActivity.class);
//                    intent.putExtra("btnRef", btnRef);
//                    intent.putExtra("fragNum", fragNum);
//                    startActivity(intent);
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
        etCount.setEnabled(state);
        spnKind.setEnabled(state);
        btnTime.setClickable(state);
        spnRepeat.setEnabled(state);
        btnDate.setClickable(state);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = new LocalDateTime(year, month, dayOfMonth, 0, 0);
        btnDate.setText(date.toString(MyUtils.dateFormat));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        hours = hourOfDay;
        minutes = minute;
        btnTime.setText(String.valueOf(hours) + ":" + String.valueOf(minutes));
    }
}
