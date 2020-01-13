package com.peterblackburn.shiftmanager.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;
import com.peterblackburn.shiftmanager.Calendar.AddEventActivity;
import com.peterblackburn.shiftmanager.Calendar.CalendarFactory;
import com.peterblackburn.shiftmanager.Calendar.Interfaces.CalendarFactoryInterface;
import com.peterblackburn.shiftmanager.Calendar.Views.DayViewContainer;
import com.peterblackburn.shiftmanager.Calendar.Views.MonthViewContainer;
import com.peterblackburn.shiftmanager.R;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.YearMonth;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.WeekFields;
import java.util.Locale;
public class CalendarFragment extends BaseFragment implements CalendarFactoryInterface {

    private static final int ADD_SHIFT_CODE = 1;

    private static CalendarFragment _instance;

    private CalendarView calendarView;
    private RelativeLayout shiftContainer;
    private RelativeLayout noShiftContainer;
    private TextView shiftTitle;
    private ImageView addShiftBtn;
    private RecyclerView eventRecycler;
    private CalendarFactory cf;

    private Context _context;



    public static CalendarFragment getInstance() {
        if(_instance == null)
            _instance = new CalendarFragment();

        return _instance;
    }

    public CalendarFragment() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_SHIFT_CODE) {
            cf.updateShifts();
            calendarView.notifyCalendarChanged();
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        shiftContainer = view.findViewById(R.id.shiftContainer);
        noShiftContainer = view.findViewById(R.id.noShiftContainer);
        eventRecycler = view.findViewById(R.id.eventRecycler);
        shiftTitle = view.findViewById(R.id.shiftTitle);
        addShiftBtn = view.findViewById(R.id.addShiftBtn);
        return view;
    }

    @Override
    public String getTitle() {
        return "Shift Calendar";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //CALENDAR SETUP

        cf = CalendarFactory.getInstance(getContext());
        cf.addFactoryInterface(this);

        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        calendarView.setup(cf.getCurrentMonth(), cf.getCurrentMonth(), firstDayOfWeek);
        calendarView.scrollToMonth(cf.getCurrentMonth());

        //EVENT ADAPTER + RECYCLER VIEW SETUP

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(_context);
        eventRecycler.setLayoutManager(layoutManager);
        eventRecycler.setAdapter(cf.getAdapter());
//        eventRecycler.addItemDecoration(new DividerItemDecoration(_context, DividerItemDecoration.VERTICAL));
//        adapter.updateShifts(shifts);

        //CALENDAR DAY AND MONTH BINDINGS
        calendarView.setDayBinder(new DayBinder<DayViewContainer>() {
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view, cf);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, @NonNull CalendarDay day) {
                container._dayText.setText(String.valueOf(day.getDate().getDayOfMonth()));
                container._day = day;
                RelativeLayout dayContainer = container._dayContainer;
                if (day.getOwner() == DayOwner.THIS_MONTH) {

                    if(day.getDate().equals(cf.getSelectedDate())) {
                        dayContainer.setBackgroundColor(getResources().getColor(R.color.darkPrimary));
                        container._dayText.setTextColor(getResources().getColor(R.color.darkOnPrimary));
                    } else {
                        dayContainer.setBackgroundColor(getResources().getColor(R.color.darkSurface));
                        container._dayText.setTextColor(getResources().getColor(R.color.darkOnSurface));
                    }

                } else {
                    container._dayText.setTextColor(Color.GRAY);
                    dayContainer.setBackgroundColor(getResources().getColor(R.color.darkSurface));
                }

            }
        });
        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthViewContainer>() {
            @Override
            public MonthViewContainer create(@NonNull View view) {
                return new MonthViewContainer(view);
            }

            @Override
            public void bind(@NonNull MonthViewContainer container, @NonNull CalendarMonth calendarMonth) {
                container.monthYearTxt.setText(getString(R.string.calendar_header_title, calendarMonth.getYearMonth().getMonth().toString(), String.valueOf(calendarMonth.getYearMonth().getYear())));
            }
        });

        //SET CALENDAR DATE TO SELECTED DATE
        cf.selectedDate(LocalDate.now());

        addShiftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(_context, AddEventActivity.class);
                intent.putExtra("selectedDate", cf.getSelectedDate().toString());
                startActivityForResult(intent, ADD_SHIFT_CODE);
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _context = null;
    }

    @Override
    public void onSelectedDateChange(LocalDate oldDate, LocalDate newDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd MMM");
        String shiftTitleStr = getString(R.string.event_title, newDate.format(formatter));
        shiftTitle.setText(shiftTitleStr);
        if(oldDate != null)
            calendarView.notifyDateChanged(oldDate);
        calendarView.notifyDateChanged(newDate);
    }

    @Override
    public void onShiftRemoved() {

    }

    @Override
    public void onShiftAdded() {

    }

    @Override
    public void onShiftsUpdated(int results) {
        if(results > 0) {
            shiftContainer.setVisibility(View.VISIBLE);
            noShiftContainer.setVisibility(View.GONE);
        } else {
            shiftContainer.setVisibility(View.GONE);
            noShiftContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMonthUpdated(YearMonth previousMonth, YearMonth currentMonth, YearMonth nextMonth) {
        calendarView.updateMonthRange(currentMonth, currentMonth);
        calendarView.scrollToMonth(currentMonth);
        calendarView.notifyCalendarChanged();
    }
}
