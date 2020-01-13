package com.peterblackburn.shiftmanager.Calendar;

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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;
import com.peterblackburn.shiftmanager.Calendar.Adapters.ShiftAdapter;
import com.peterblackburn.shiftmanager.Calendar.Interfaces.CalendarInterface;
import com.peterblackburn.shiftmanager.Calendar.Interfaces.EventInterface;
import com.peterblackburn.shiftmanager.Calendar.Views.DayViewContainer;
import com.peterblackburn.shiftmanager.Calendar.Views.MonthViewContainer;
import com.peterblackburn.shiftmanager.Enums.MonthUpdate;
import com.peterblackburn.shiftmanager.Fragments.BaseFragment;
import com.peterblackburn.shiftmanager.Helper.RealmHelper;
import com.peterblackburn.shiftmanager.R;
import com.peterblackburn.shiftmanager.Realm.Objects.Shift;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.YearMonth;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.WeekFields;
import java.util.Locale;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class CalendarFragment extends BaseFragment implements CalendarInterface, EventInterface {

    static final int ADD_SHIFT_CODE = 1;

    private static CalendarFragment _instance;

    private CalendarView calendarView;
    private RelativeLayout shiftContainer;
    private RelativeLayout noShiftContainer;
    private TextView shiftTitle;
    private ImageView addShiftBtn;
    private LocalDate selectedDate;
    private RecyclerView eventRecycler;
    private Context _context;
    private ShiftAdapter adapter;
    private Realm realm = Realm.getDefaultInstance();

    private YearMonth currentMonth;
    private YearMonth nextMonth;
    private YearMonth prevMonth;

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
            updateShifts();
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
        //ADD A RANDOM SHIFT FOR TESTING
//        Shift shift = new Shift(LocalDateTime.of(2020, 1,5,18, 0), LocalDateTime.of(2020, 1, 5, 22, 0));
//        shift.setId(RealmPrimaryKey.getInstance().nextPrimaryKey());
//        realm.beginTransaction();
//        final Shift managedShift = realm.copyToRealm(shift);
//        realm.commitTransaction();

//        shifts.add_event(new Shift(LocalDateTime.of(2020, 1,5,10, 0), LocalDateTime.of(2020, 1, 5, 15, 0)));

        //CALENDAR SETUP
        currentMonth = YearMonth.now();
        prevMonth = currentMonth.minusMonths(1);
        nextMonth = currentMonth.plusMonths(1);
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        calendarView.setup(currentMonth, currentMonth, firstDayOfWeek);
        calendarView.scrollToMonth(currentMonth);

        //EVENT ADAPTER + RECYCLER VIEW SETUP
        adapter = new ShiftAdapter(_context, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(_context);
        eventRecycler.setLayoutManager(layoutManager);
        eventRecycler.setAdapter(adapter);
//        eventRecycler.addItemDecoration(new DividerItemDecoration(_context, DividerItemDecoration.VERTICAL));
//        adapter.updateShifts(shifts);
        adapter.notifyDataSetChanged();

        //CALENDAR DAY AND MONTH BINDINGS
        calendarView.setDayBinder(new DayBinder<DayViewContainer>() {
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view, CalendarFragment.this);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, @NonNull CalendarDay day) {
                container._dayText.setText(String.valueOf(day.getDate().getDayOfMonth()));
                container._day = day;
                RelativeLayout dayContainer = container._dayContainer;
                if (day.getOwner() == DayOwner.THIS_MONTH) {

                    if(day.getDate().equals(selectedDate)) {
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
        selectedDate(LocalDate.now());

        addShiftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(_context, AddEventActivity.class);
                intent.putExtra("selectedDate", selectedDate.toString());
                startActivityForResult(intent, ADD_SHIFT_CODE);
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _context = null;
    }

    @Override
    public void selectedDate(LocalDate date) {
        if(selectedDate == null) {
            selectedDate = date;
            calendarView.notifyDateChanged(selectedDate);
        } else {
            if (date != selectedDate) {
                LocalDate oldDate = selectedDate;
                selectedDate = date;
                calendarView.notifyDateChanged(oldDate);
                calendarView.notifyDateChanged(selectedDate);
            }
        }
        updateShifts();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd MMM");
        String shiftTitleStr = getString(R.string.event_title, selectedDate.format(formatter));
        shiftTitle.setText(shiftTitleStr);
    }

    private void updateShifts() {

        final RealmResults<Shift> shiftResults = realm.where(Shift.class).contains("startTime", selectedDate.toString()).findAll().sort("startTime", Sort.ASCENDING);
        adapter.updateShifts(shiftResults);

        //EVENT CONTAINER VISIBILITY
        if(shiftResults.size() > 0) {
            shiftContainer.setVisibility(View.VISIBLE);
            noShiftContainer.setVisibility(View.GONE);
        } else {
            shiftContainer.setVisibility(View.GONE);
            noShiftContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void removeShift(Shift shift) {
        RealmHelper.getInstance().deleteShift(shift);
        updateShifts();
    }

    public void updateMonth(MonthUpdate update) {
        switch (update) {
            case NEXT_MONTH:
                prevMonth = currentMonth;
                currentMonth = nextMonth;
                nextMonth = currentMonth.plusMonths(1);
                break;
            case PREVIOUS_MONTH:
                nextMonth = currentMonth;
                currentMonth = prevMonth;
                prevMonth = currentMonth.minusMonths(1);
                break;
        }
        calendarView.updateMonthRange(currentMonth, currentMonth);
        if(currentMonth.atDay(LocalDate.now().getDayOfMonth()).equals(LocalDate.now())) {
            selectedDate(LocalDate.now());
        } else {
            selectedDate(currentMonth.atDay(1));
        }
        calendarView.scrollToMonth(currentMonth);
        updateShifts();
        calendarView.notifyCalendarChanged();
    }



}
