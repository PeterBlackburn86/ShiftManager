package com.peterblackburn.shiftmanager.Calendar.Views;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kizitonwose.calendarview.ui.ViewContainer;
import com.peterblackburn.shiftmanager.R;

public class MonthViewContainer extends ViewContainer {
    public TextView monthYearTxt;
//    public TextView monthText;

    public MonthViewContainer(@NonNull View view) {
        super(view);
        monthYearTxt = view.findViewById(R.id.monthYearTxt);
//        monthText = view.findViewById(R.id.calendarMonthText);
    }
}
