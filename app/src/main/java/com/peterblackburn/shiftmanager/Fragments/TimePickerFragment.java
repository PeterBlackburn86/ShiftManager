package com.peterblackburn.shiftmanager.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.peterblackburn.shiftmanager.Calendar.Interfaces.TimeDateInterface;

import org.threeten.bp.LocalTime;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TimeDateInterface _listener;
    private String _id;

    public static TimePickerFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("picker_id", id);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = 0;

        _id = getArguments().getString("picker_id");

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onAttach(Activity activity) {
        // when the fragment is initially shown (i.e. attached to the activity),
        // cast the activity to the callback interface type
        super.onAttach(activity);
        try {
            _listener = (TimeDateInterface) activity;
        } catch(ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement " + TimeDateInterface.class.getName());
        }
    }


    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        _listener.onTimePicked(LocalTime.of(i, i1), _id);
    }
}
