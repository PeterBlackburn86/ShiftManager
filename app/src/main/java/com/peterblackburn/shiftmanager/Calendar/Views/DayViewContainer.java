package com.peterblackburn.shiftmanager.Calendar.Views;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.ViewContainer;
import com.peterblackburn.shiftmanager.Calendar.CalendarFactory;
import com.peterblackburn.shiftmanager.R;

public class DayViewContainer extends ViewContainer {
    public TextView _dayText;
    public RelativeLayout _dayContainer;
    public CalendarDay _day;
//    private CalendarInterface _calendarInterface;

    public DayViewContainer(@NonNull View view) {
        super(view);
//        _calendarInterface = calendarInterface;
        _dayText = view.findViewById(R.id.calendarDayText);
        _dayContainer = view.findViewById(R.id.calendarDayContainer);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(_day.getOwner() == DayOwner.THIS_MONTH) {
//                    if(_calendarInterface != null) {
//                        _calendarInterface.setSelectedDate(_day.getDate());
//                    }
                    CalendarFactory.getInstance().setSelectedDate(_day.getDate());
                }
            }
        });


    }
}