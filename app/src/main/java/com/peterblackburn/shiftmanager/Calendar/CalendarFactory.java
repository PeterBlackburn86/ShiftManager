package com.peterblackburn.shiftmanager.Calendar;

import android.content.Context;
import android.view.View;

import com.peterblackburn.shiftmanager.Calendar.Adapters.ShiftAdapter;
import com.peterblackburn.shiftmanager.Calendar.Interfaces.CalendarFactoryInterface;
import com.peterblackburn.shiftmanager.Calendar.Interfaces.CalendarInterface;
import com.peterblackburn.shiftmanager.Calendar.Interfaces.EventInterface;
import com.peterblackburn.shiftmanager.Enums.MonthUpdate;
import com.peterblackburn.shiftmanager.Realm.Helper.RealmHelper;
import com.peterblackburn.shiftmanager.Realm.Objects.Shift;

import org.threeten.bp.LocalDate;
import org.threeten.bp.YearMonth;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class CalendarFactory implements CalendarInterface, EventInterface {

    private static CalendarFactory _instance;

    private ArrayList<CalendarFactoryInterface> _interfaces = new ArrayList<>();
    private LocalDate _selectedDate;
    private LocalDate _oldDate;
    private ShiftAdapter _adapter;
    private YearMonth _currentMonth;
    private YearMonth _nextMonth;
    private YearMonth _prevMonth;
    private Realm _realm;

    private CalendarFactory(Context context) {
        _currentMonth = YearMonth.now();
        _prevMonth = _currentMonth.minusMonths(1);
        _nextMonth = _currentMonth.plusMonths(1);
        _adapter = new ShiftAdapter(context, this);
        _realm = Realm.getDefaultInstance();
    }

    public static CalendarFactory getInstance(Context context) {
        if(_instance == null)
            _instance = new CalendarFactory(context);

        return _instance;
    }

    public YearMonth getCurrentMonth() { return _currentMonth; }
    public YearMonth getNextMonth() { return _nextMonth; }
    public YearMonth getPrevMonth() { return _prevMonth; }
    public LocalDate getSelectedDate() { return _selectedDate; }
    public ShiftAdapter getAdapter() { return _adapter; }

    public void addFactoryInterface(CalendarFactoryInterface calendarInterface) {
        if(!_interfaces.contains(calendarInterface)) {
            _interfaces.add(calendarInterface);
        }
    }

    public void removeFactoryInterface(CalendarFactoryInterface calendarInterface) {
        if(_interfaces.contains(calendarInterface)) {
            _interfaces.remove(calendarInterface);
        }
    }

    public void updateMonth(MonthUpdate monthUpdate) {
        switch (monthUpdate) {
            case NEXT_MONTH:
                _prevMonth = _currentMonth;
                _currentMonth = _nextMonth;
                _nextMonth = _currentMonth.plusMonths(1);
                break;
            case PREVIOUS_MONTH:
                _nextMonth = _currentMonth;
                _currentMonth = _prevMonth;
                _prevMonth = _currentMonth.minusMonths(1);
                break;
        }if(_currentMonth.atDay(LocalDate.now().getDayOfMonth()).equals(LocalDate.now())) {
            selectedDate(LocalDate.now());
        } else {
            selectedDate(_currentMonth.atDay(1));
        }
        updateShifts();
        notifyMonthUpdated();
    }
    private void notifyMonthUpdated() {
        for(CalendarFactoryInterface cfInterface : _interfaces) {
            cfInterface.onMonthUpdated(_prevMonth, _currentMonth, _nextMonth);
        }
    }

    @Override
    public void selectedDate(LocalDate date) {
        if(_selectedDate == null) {
            _selectedDate = date;
        } else {
            if (date != _selectedDate) {
                _oldDate = _selectedDate;
                _selectedDate = date;
            }
        }
        updateShifts();
        notifyDateChanged();
    }
    private void notifyDateChanged() {
        for(CalendarFactoryInterface cfInterface : _interfaces) {
            cfInterface.onSelectedDateChange(_oldDate, _selectedDate);
        }
    }

    @Override
    public void removeShift(Shift shift) {
        RealmHelper.getInstance().deleteShift(shift);
        updateShifts();
        notifyShiftRemoved();
    }
    private void notifyShiftRemoved() {
        for(CalendarFactoryInterface cfInterface : _interfaces) {
            cfInterface.onShiftRemoved();
        }
    }

    public void updateShifts() {

        final RealmResults<Shift> shiftResults = _realm.where(Shift.class).contains("startTime", _selectedDate.toString()).findAll().sort("startTime", Sort.ASCENDING);
        _adapter.updateShifts(shiftResults);
        notifyShiftsUpdated(shiftResults.size());
    }
    private void notifyShiftsUpdated(int results) {
        for(CalendarFactoryInterface cfInterface : _interfaces) {
            cfInterface.onShiftsUpdated(results);
        }
    }
}
