package com.peterblackburn.shiftmanager.Fragments;

import android.content.Context;
import android.content.Intent;
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
import com.peterblackburn.shiftmanager.Calendar.AddEventActivity;
import com.peterblackburn.shiftmanager.Calendar.CalendarFactory;
import com.peterblackburn.shiftmanager.Calendar.Interfaces.CalendarFactoryInterface;
import com.peterblackburn.shiftmanager.Events.EventFactory;
import com.peterblackburn.shiftmanager.Events.Interfaces.EventFactoryInterface;
import com.peterblackburn.shiftmanager.R;
import org.threeten.bp.LocalDate;
import org.threeten.bp.YearMonth;
import org.threeten.bp.format.DateTimeFormatter;

public class CalendarFragment extends BaseFragment implements CalendarFactoryInterface, EventFactoryInterface {

    private static final int ADD_SHIFT_CODE = 1;

    private static CalendarFragment _instance;

    private RelativeLayout _shiftContainer;
    private RelativeLayout _noShiftContainer;
    private TextView _shiftTitle;
    private TextView _monthYearTxt;
    private ImageView _addShiftBtn;
    private RecyclerView _eventRecycler;
    private CalendarFactory _calendarFactory;
    private EventFactory _eventFactory;
    private CalendarView _calendarView;
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
            _eventFactory.updateShifts();
        }
        System.out.println("ACTIVITY RESULT");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        System.out.println("CREATE VIEW");
        _calendarView = view.findViewById(R.id.calendarView);
        _shiftContainer = view.findViewById(R.id.shiftContainer);
        _noShiftContainer = view.findViewById(R.id.noShiftContainer);
        _eventRecycler = view.findViewById(R.id.eventRecycler);
        _shiftTitle = view.findViewById(R.id.shiftTitle);
        _addShiftBtn = view.findViewById(R.id.addShiftBtn);
        _monthYearTxt = view.findViewById(R.id.monthYearTxt);
        return view;
    }

    @Override
    public String getTitle() {
        return "Shift Calendar";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("ACTIVITY CREATED");

        //CALENDAR + EVENTS SETUP
        _calendarFactory = CalendarFactory.getInstance();
        _eventFactory = EventFactory.getInstance();

        _calendarFactory.initCalendar(_context, _calendarView, this);
        _eventFactory.initEventFactory(_context, _eventRecycler, this);

        if(_calendarFactory.getSelectedDate() == null) {
            _calendarFactory.setSelectedDate(LocalDate.now());
        }
        _eventFactory.updateShifts();

        //CALENDAR DAY AND MONTH BINDINGS


        _addShiftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(_context, AddEventActivity.class);
                intent.putExtra("setSelectedDate", _calendarFactory.getSelectedDate().toString());
                startActivityForResult(intent, ADD_SHIFT_CODE);
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        System.out.println("ON ATTACH");
        super.onAttach(context);
        _context = context;
        if(_calendarFactory != null && _eventFactory != null) {
            _calendarFactory.addFactoryInterface(this);
            _eventFactory.addFactoryInterface(this);
        }
    }

    @Override
    public void onDetach() {
        System.out.println("ON DETACH");
        super.onDetach();
        if(_calendarFactory != null && _eventFactory != null) {
            _calendarFactory.removeFactoryInterface(this);
            _eventFactory.removeFactoryInterface(this);
            _context = null;
        }
    }

    @Override
    public void onSelectedDateChange(LocalDate oldDate, LocalDate newDate) {
        updateEventTitle(newDate);
        _eventFactory.updateShifts();
    }

    @Override
    public void onMonthUpdated(YearMonth previousMonth, YearMonth currentMonth, YearMonth nextMonth) {
        System.out.println("ON MONTH UPDATED");
        _monthYearTxt.setText(_context.getString(R.string.calendar_header_title, currentMonth.getMonth().toString(), String.valueOf(currentMonth.getYear())));
    }

    @Override
    public void onShiftRemoved() {
        System.out.println("ON SHIFT REMOVED");
    }

    @Override
    public void onShiftAdded() {
        System.out.println("ON SHIFT ADDED");
    }

    @Override
    public void onShiftsUpdated(int results) {
        System.out.println("ON SHIFT UPDATED");
        System.out.println("RESULTS: " + results);
        if(results > 0) {
            _shiftContainer.setVisibility(View.VISIBLE);
            _noShiftContainer.setVisibility(View.GONE);
        } else {
            _shiftContainer.setVisibility(View.GONE);
            _noShiftContainer.setVisibility(View.VISIBLE);
        }
        updateEventTitle(_calendarFactory.getSelectedDate());
    }

    private void updateEventTitle(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd MMM");
        String shiftTitleStr = _context.getString(R.string.event_title, date.format(formatter));
        _shiftTitle.setText(shiftTitleStr);
    }
}
