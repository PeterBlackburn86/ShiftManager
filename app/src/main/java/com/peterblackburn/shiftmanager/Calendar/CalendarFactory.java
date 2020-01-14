package com.peterblackburn.shiftmanager.Calendar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;
import com.peterblackburn.shiftmanager.Calendar.Interfaces.CalendarFactoryInterface;
import com.peterblackburn.shiftmanager.Calendar.Views.DayViewContainer;
import com.peterblackburn.shiftmanager.Calendar.Views.MonthViewContainer;
import com.peterblackburn.shiftmanager.R;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.YearMonth;
import org.threeten.bp.temporal.WeekFields;

import java.util.ArrayList;
import java.util.Locale;

public class CalendarFactory {

    private static CalendarFactory _instance;

    private ArrayList<CalendarFactoryInterface> _interfaces = new ArrayList<>();
    private LocalDate _selectedDate;
    private LocalDate _oldDate;
    private YearMonth _currentMonth;
    private YearMonth _nextMonth;
    private YearMonth _prevMonth;
    private Context _context;
    private CalendarView _calendarView;

    private CalendarFactory() {
    }

    public static CalendarFactory getInstance() {
        if(_instance == null)
            _instance = new CalendarFactory();

        return _instance;
    }

    public void initCalendar(Context context, CalendarView calendarView, CalendarFactoryInterface calendarFactoryInterface) {
        _context = context;
        addFactoryInterface(calendarFactoryInterface);
        _calendarView = calendarView;

        _currentMonth = YearMonth.now();
        _prevMonth = _currentMonth.minusMonths(1);
        _nextMonth = _currentMonth.plusMonths(1);
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.UK).getFirstDayOfWeek();
        _calendarView.setup(_currentMonth, _currentMonth, firstDayOfWeek);
        _calendarView.scrollToMonth(_currentMonth);
        notifyMonthUpdated();

        _calendarView.setDayBinder(new DayBinder<DayViewContainer>() {
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, @NonNull CalendarDay day) {
                container._dayText.setText(String.valueOf(day.getDate().getDayOfMonth()));
                container._day = day;
                RelativeLayout dayContainer = container._dayContainer;

                Resources.Theme theme = _context.getTheme();
                int[] attrs = {R.attr.surface, R.attr.surfacePrimary, R.attr.textOnSurface, R.attr.textOnPrimary};
                TypedArray values = theme.obtainStyledAttributes(attrs);

                int[] backAttrs = { android.R.attr.background };
                int[] textAttrs = { android.R.attr.textColor };

                int surfaceColor = Color.BLACK;
                int primarySurfaceColor = Color.BLACK;
                int textOnSurface = Color.BLACK;
                int textOnPrimary = Color.BLACK;


                for(int i=0; i < attrs.length; i++) {
                    TypedArray results;

                    switch (i) {
                        case 0:
                            results = theme.obtainStyledAttributes(values.getResourceId(i,0), backAttrs);
                            surfaceColor = results.getColor(0, Color.BLACK);
                            break;
                        case 1:
                            results = theme.obtainStyledAttributes(values.getResourceId(i,0), backAttrs);
                            primarySurfaceColor = results.getColor(0, Color.BLACK);
                            break;
                        case 2:
                            results = theme.obtainStyledAttributes(values.getResourceId(i,0), textAttrs);
                            textOnSurface = results.getColor(0, Color.BLACK);
                            break;
                        case 3:
                            results = theme.obtainStyledAttributes(values.getResourceId(i,0), textAttrs);
                            textOnPrimary = results.getColor(0, Color.BLACK);
                            break;
                    }
                }



                if (day.getOwner() == DayOwner.THIS_MONTH) {

                    if(day.getDate().equals(_selectedDate)) {
                        dayContainer.setBackgroundColor(primarySurfaceColor);
                        container._dayText.setTextColor(textOnPrimary);
                    } else {
                        dayContainer.setBackgroundColor(surfaceColor);
                        container._dayText.setTextColor(textOnSurface);
                    }

                } else {
                    container._dayText.setTextColor(_context.getColor(R.color.calendarUnusedDaysTxt));
                    dayContainer.setBackgroundColor(_context.getColor(R.color.calendarUnusedDays));
                }

            }
        });
        _calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthViewContainer>() {
            @Override
            public MonthViewContainer create(@NonNull View view) {
                return new MonthViewContainer(view);
            }

            @Override
            public void bind(@NonNull MonthViewContainer container, @NonNull CalendarMonth calendarMonth) {
//                container.monthYearTxt.setText(_context.getString(R.string.calendar_header_title, calendarMonth.getYearMonth().getMonth().toString(), String.valueOf(calendarMonth.getYearMonth().getYear())));
            }
        });
    }

    public YearMonth getCurrentMonth() { return _currentMonth; }
    public YearMonth getNextMonth() { return _nextMonth; }
    public YearMonth getPrevMonth() { return _prevMonth; }
    public LocalDate getSelectedDate() { return _selectedDate; }

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

    public void increaseMonth() {
        _prevMonth = _currentMonth;
        _currentMonth = _nextMonth;
        _nextMonth = _currentMonth.plusMonths(1);
        updateMonth();
    }

    public void decreaseMonth() {
        _nextMonth = _currentMonth;
        _currentMonth = _prevMonth;
        _prevMonth = _currentMonth.minusMonths(1);
        updateMonth();
    }
    private void updateMonth() {
        if(_currentMonth.atDay(LocalDate.now().getDayOfMonth()).equals(LocalDate.now())) {
            setSelectedDate(LocalDate.now());
        } else {
            setSelectedDate(_currentMonth.atDay(1));
        }
        _calendarView.updateMonthRange(_currentMonth, _currentMonth);
        _calendarView.scrollToMonth(_currentMonth);
        _calendarView.notifyCalendarChanged();
        notifyMonthUpdated();
    }
    private void notifyMonthUpdated() {
        for(CalendarFactoryInterface cfInterface : _interfaces) {
            cfInterface.onMonthUpdated(_prevMonth, _currentMonth, _nextMonth);
        }
    }

    public void setSelectedDate(LocalDate date) {
        if(_selectedDate == null) {
            _selectedDate = date;
        } else {
            if (date != _selectedDate) {
                _oldDate = _selectedDate;
                _selectedDate = date;
            }
        }
        if(_oldDate != null)
            _calendarView.notifyDateChanged(_oldDate);
        _calendarView.notifyDateChanged(_selectedDate);
        notifyDateChanged();
    }
    private void notifyDateChanged() {
        for(CalendarFactoryInterface cfInterface : _interfaces) {
            cfInterface.onSelectedDateChange(_oldDate, _selectedDate);
        }
    }




}
