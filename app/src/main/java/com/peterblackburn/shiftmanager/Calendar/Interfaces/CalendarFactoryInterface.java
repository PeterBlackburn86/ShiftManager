package com.peterblackburn.shiftmanager.Calendar.Interfaces;

import org.threeten.bp.LocalDate;
import org.threeten.bp.YearMonth;

public interface CalendarFactoryInterface {

    //Calendar Events
    void onSelectedDateChange(LocalDate oldDate, LocalDate newDate);

    //Shift Events
    void onShiftRemoved();
    void onShiftAdded();
    void onShiftsUpdated(int results);
    void onMonthUpdated(YearMonth previousMonth, YearMonth currentMonth, YearMonth nextMonth);
}
