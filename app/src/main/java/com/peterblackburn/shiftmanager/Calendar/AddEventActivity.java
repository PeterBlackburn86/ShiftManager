package com.peterblackburn.shiftmanager.Calendar;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.peterblackburn.shiftmanager.Calendar.Interfaces.TimeDateInterface;
import com.peterblackburn.shiftmanager.Fragments.TimePickerFragment;
import com.peterblackburn.shiftmanager.Helper.RealmHelper;
import com.peterblackburn.shiftmanager.R;
import com.peterblackburn.shiftmanager.Realm.Objects.Shift;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.sql.Time;

public class AddEventActivity extends FragmentActivity implements TimeDateInterface {

    TextView addShiftDate;
    TextView addShiftStartTime;
    TextView addShiftEndTime;
    Button addShiftBtn;

    LocalDate date;
    LocalTime startTime = LocalTime.now();
    LocalTime endTime = LocalTime.now();
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_add_activity);

        Intent intent = getIntent();

        selectedDate = intent.getStringExtra("selectedDate");
//        System.out.println("SELECTED DATE:" + selectedDate + " | SIZE:" + selectedDate.length());
        date = LocalDate.parse(selectedDate);
//        date = LocalDateTime.parse(selectedDate).toLocalDate();

        addShiftBtn = findViewById(R.id.submitShiftBtn);
        addShiftDate = findViewById(R.id.addShiftDate);
        addShiftStartTime = findViewById(R.id.addShiftStartTime);
        addShiftEndTime = findViewById(R.id.addShiftEndTime);

        addShiftDate.setText(date.toString());

        addShiftStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = TimePickerFragment.newInstance("startTimePicker");
                timePicker.show(getSupportFragmentManager(), "startTimePicker");
            }
        });


        addShiftEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = TimePickerFragment.newInstance("endTimePicker");
                timePicker.show(getSupportFragmentManager(), "endTimePicker");
            }
        });


        addShiftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Shift shift = new Shift();
                shift.setId(RealmHelper.getInstance().nextPrimaryKey());

                LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
                LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

//                LocalDateTime startTime = LocalDateTime.parse(String.join(addShiftDate.getText() , addShiftStartTime.getText()));
//                LocalDateTime endTime = LocalDateTime.parse(String.join(addShiftDate.getText() , addShiftEndTime.getText()));

                shift.setStartTime(startDateTime);
                shift.setEndTime(endDateTime);

                RealmHelper.getInstance().addShifts(shift);
                finish();
            }
        });
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void onTimePicked(LocalTime time, String id) {
        switch (id) {
            case "startTimePicker":
                startTime = time;
                addShiftStartTime.setText(startTime.toString());
                break;
            case "endTimePicker":
                endTime = time;
                addShiftEndTime.setText(endTime.toString());
                break;
        }
    }
}
