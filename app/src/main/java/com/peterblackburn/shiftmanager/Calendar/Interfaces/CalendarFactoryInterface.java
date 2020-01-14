package com.peterblackburn.shiftmanager.Calendar.Interfaces;

import org.threeten.bp.LocalDate;
import org.threeten.bp.YearMonth;

public interface CalendarFactoryInterface {

    //Calendar Events
    void onSelectedDateChange(LocalDate oldDate, LocalDate newDate);
    void onMonthUpdated(YearMonth previousMonth, YearMonth currentMonth, YearMonth nextMonth);

}
