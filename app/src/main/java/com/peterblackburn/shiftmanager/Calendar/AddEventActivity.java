package com.peterblackburn.shiftmanager.Calendar;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.peterblackburn.shiftmanager.Calendar.Interfaces.TimeDateInterface;
import com.peterblackburn.shiftmanager.Events.Enums.EventType;
import com.peterblackburn.shiftmanager.Events.Models.Break;
import com.peterblackburn.shiftmanager.Events.Models.Event;
import com.peterblackburn.shiftmanager.Events.Models.EventTemplate;
import com.peterblackburn.shiftmanager.Fragments.TimePickerFragment;
import com.peterblackburn.shiftmanager.RealmHelper;
import com.peterblackburn.shiftmanager.R;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddEventActivity extends FragmentActivity implements TimeDateInterface {

    private static final String EVENT_START_TIME = "eventStartTime";
    private static final String EVENT_END_TIME = "eventEndTime";
    private static final String BREAK_START_TIME = "breakStartTime";
    private static final String BREAK_END_TIME = "breakEndTime";

    private ArrayList<Break> tempBreaks = new ArrayList<>();
    Switch _addEventAllDay;
    Switch _addBreakIsPaid;
    Spinner _addEventType;
    LinearLayout _addEventStartContainer;
    LinearLayout _addEventEndContainer;
    LinearLayout _addEventBreakContainer;
    LinearLayout _addEventDateContainer;
    LinearLayout _templateNameContainer;
    TextView _addEventDate;
    TextView _addEventStartTime;
    TextView _addEventEndTime;
    TextView _addBreakStartTime;
    TextView _addBreakEndTime;
    TextView _addEventTitle;
    Button _addEventBtn;
    Button _addBreakBtn;
    EditText _templateName;

    LocalDate _date;
    LocalTime _eventStartTime = LocalTime.now();
    LocalTime _eventEndTime = LocalTime.now();
    LocalTime _breakStartTime = LocalTime.now();
    LocalTime _breakEndTime = LocalTime.now();
    boolean _isAllDay;
    boolean _isBreakPaid;
    boolean _isTemplate;
    EventType _eventType;
    String _selectedDate;
    String _templateNameString;
    List<EventType> _typeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MaterialDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_add_activity);

        Intent intent = getIntent();
        _selectedDate = intent.getStringExtra("setSelectedDate");
        if(_selectedDate.equals(""))
            _selectedDate = null;
        _isTemplate = intent.getBooleanExtra("setIsTemplate", false);

        if(_selectedDate != null)
            _date = LocalDate.parse(_selectedDate);
        else
            _date = LocalDate.now();

        _addEventAllDay = findViewById(R.id.addEventAllDay);
        _addEventType = findViewById(R.id.addEventType);
        _addEventStartContainer = findViewById(R.id.addEventStartContainer);
        _addEventEndContainer = findViewById(R.id.addEventEndContainer);
        _addEventBtn = findViewById(R.id.submitEventBtn);
        _addEventDate = findViewById(R.id.addEventDate);
        _addEventStartTime = findViewById(R.id.addEventStartTime);
        _addEventEndTime = findViewById(R.id.addEventEndTime);
        _addEventBreakContainer = findViewById(R.id.addEventBreakContainer);
        _addBreakIsPaid = findViewById(R.id.addBreakIsPaid);
        _addBreakStartTime = findViewById(R.id.addBreakStartTime);
        _addBreakEndTime = findViewById(R.id.addBreakEndTime);
        _addBreakBtn = findViewById(R.id.addBreakBtn);
        _addEventTitle = findViewById(R.id.addEventTitle);
        _addEventDateContainer = findViewById(R.id.addEventDateContainer);
        _templateNameContainer = findViewById(R.id.templateNameContainer);
        _templateName = findViewById(R.id.templateName);

        if(!_isTemplate) {
            _addEventDateContainer.setVisibility(View.VISIBLE);
            _addEventDate.setText(_date.toString());
            _addEventBtn.setText(R.string.add_event_submit);
            _addEventTitle.setText(R.string.add_event_title);
            _templateNameContainer.setVisibility(View.GONE);
        } else {
            _addEventDateContainer.setVisibility(View.GONE);
            _addEventBtn.setText(R.string.create_event_template_submit);
            _addEventTitle.setText(R.string.create_event_template_title);
            _templateNameContainer.setVisibility(View.VISIBLE);
        }

        _typeList = new ArrayList<>(Arrays.asList(EventType.values()));



        ArrayAdapter<EventType> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _addEventType.setAdapter(typeAdapter);

        _addEventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                _eventType = _typeList.get(i);

                switch (_eventType) {
                    case PAY_DAY:
                        _addEventBreakContainer.setVisibility(View.GONE);
                        _addEventAllDay.setChecked(true);
                        break;
                    case OVERTIME:
                        _addEventBreakContainer.setVisibility(View.VISIBLE);
                        break;
                    case REGULAR_SHIFT:
                        _addEventBreakContainer.setVisibility(View.VISIBLE);
                        break;
                }
                System.out.println("SELECTED TYPE: " + _eventType.getReadableName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        _addBreakIsPaid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                _isBreakPaid = b;
            }
        });

        _addEventAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                _isAllDay = b;
                if(_isAllDay) {
                    _addEventStartContainer.setVisibility(View.GONE);
                    _addEventEndContainer.setVisibility(View.GONE);
                } else {
                    _addEventStartContainer.setVisibility(View.VISIBLE);
                    _addEventEndContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        _addEventStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = TimePickerFragment.newInstance(EVENT_START_TIME);
                timePicker.show(getSupportFragmentManager(), EVENT_START_TIME);
            }
        });


        _addEventEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = TimePickerFragment.newInstance(EVENT_END_TIME);
                timePicker.show(getSupportFragmentManager(), EVENT_END_TIME);
            }
        });

        _addBreakStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = TimePickerFragment.newInstance(BREAK_START_TIME);
                timePicker.show(getSupportFragmentManager(), BREAK_START_TIME);
            }
        });


        _addBreakEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = TimePickerFragment.newInstance(BREAK_END_TIME);
                timePicker.show(getSupportFragmentManager(), BREAK_END_TIME);
            }
        });


        _addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!_isTemplate) {
                    Event event = new Event();
                    event.setId(RealmHelper.getInstance().nextPrimaryKey(RealmHelper.EVENT_TABLE));

                    event.setIsAllDay(_isAllDay);
                    event.setEventType(_eventType);
                    event.setHasBreaks(tempBreaks.size()!=0);
                    if(event.hasBreaks()) {
                        for(Break b : tempBreaks) {
                            event.addBreak(b);
                        }
                    }

                    if(_isAllDay) {
                        LocalDateTime startDateTime = LocalDateTime.of(_date, LocalTime.MIN);
                        LocalDateTime endDateTime = LocalDateTime.of(_date, LocalTime.MAX);
                        event.setStartTime(startDateTime);
                        event.setEndTime(endDateTime);
                    } else {
                        LocalDateTime startDateTime = LocalDateTime.of(_date, _eventStartTime);
                        LocalDateTime endDateTime = LocalDateTime.of(_date, _eventEndTime);
                        event.setStartTime(startDateTime);
                        event.setEndTime(endDateTime);
                    }


                    RealmHelper.getInstance().addEvents(event);
                } else {
                    EventTemplate template = new EventTemplate();
                    template.setId(RealmHelper.getInstance().nextPrimaryKey(RealmHelper.TEMPLATE_TABLE));

                    template.setTemplateName(_templateName.getText().toString());
                    template.setIsAllDay(_isAllDay);
                    template.setEventType(_eventType);
                    template.setHasBreaks(tempBreaks.size()!=0);
                    if(template.hasBreaks()) {
                        for(Break b : tempBreaks) {
                            template.addBreak(b);
                        }
                    }

                    if(_isAllDay) {
                        LocalDateTime startDateTime = LocalDateTime.of(_date, LocalTime.MIN);
                        LocalDateTime endDateTime = LocalDateTime.of(_date, LocalTime.MAX);
                        template.setStartTime(startDateTime);
                        template.setEndTime(endDateTime);
                    } else {
                        LocalDateTime startDateTime = LocalDateTime.of(_date, _eventStartTime);
                        LocalDateTime endDateTime = LocalDateTime.of(_date, _eventEndTime);
                        template.setStartTime(startDateTime);
                        template.setEndTime(endDateTime);
                    }


                    RealmHelper.getInstance().addEventTemplates(template);
                }

                finish();
            }
        });

        _addBreakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Break b = new Break();
                b.setBreakStart(_breakStartTime);
                b.setBreakEnd(_breakEndTime);
                b.setBreakPaid(_isBreakPaid);
                tempBreaks.add(b);
            }
        });

        _addEventType.setSelection(0);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void onTimePicked(LocalTime time, String id) {
        switch (id) {
            case EVENT_START_TIME:
                _eventStartTime = time;
                _addEventStartTime.setText(_eventStartTime.toString());
                break;
            case EVENT_END_TIME:
                _eventEndTime = time;
                _addEventEndTime.setText(_eventEndTime.toString());
                break;
            case BREAK_START_TIME:
                _breakStartTime = time;
                _addBreakStartTime.setText(_breakStartTime.toString());
                break;
            case BREAK_END_TIME:
                _breakEndTime = time;
                _addBreakEndTime.setText(_breakEndTime.toString());
                break;
        }
    }
}
