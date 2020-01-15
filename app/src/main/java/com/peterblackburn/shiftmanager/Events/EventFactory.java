package com.peterblackburn.shiftmanager.Events;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.peterblackburn.shiftmanager.Calendar.CalendarFactory;
import com.peterblackburn.shiftmanager.Events.Interfaces.EventInterface;
import com.peterblackburn.shiftmanager.Events.Adapters.EventAdapter;
import com.peterblackburn.shiftmanager.Events.Interfaces.EventFactoryInterface;
import com.peterblackburn.shiftmanager.Events.Models.Event;
import com.peterblackburn.shiftmanager.RealmHelper;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class EventFactory implements EventInterface {

    private static EventFactory _instance;

    private EventAdapter _adapter;
    private Realm _realm;
    private Context _context;
    private RecyclerView _recyclerView;
    private ArrayList<EventFactoryInterface> _interfaces = new ArrayList<>();

    public static EventFactory getInstance() {
        if(_instance == null)
            _instance = new EventFactory();
        return _instance;
    }

    private EventFactory() {
        _realm = Realm.getDefaultInstance();
    }

    public void initEventFactory(Context context, RecyclerView recyclerView, EventFactoryInterface eventFactoryInterface) {

        _context = context;
        addFactoryInterface(eventFactoryInterface);
        _adapter = new EventAdapter(context, this);
        _recyclerView = recyclerView;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(_context);
        _recyclerView.setLayoutManager(layoutManager);
        _recyclerView.setAdapter(_adapter);//
        // _recyclerView.addItemDecoration(new DividerItemDecoration(_context, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void removeEvent(Event event) {
        RealmHelper.getInstance().deleteShift(event);
        updateShifts();
        notifyShiftRemoved();
    }
    private void notifyShiftRemoved() {
        for(EventFactoryInterface efInterface : _interfaces) {
            efInterface.onShiftRemoved();
        }
    }

    public void addFactoryInterface(EventFactoryInterface eventFactoryInterface) {
        if(!_interfaces.contains(eventFactoryInterface)) {
            _interfaces.add(eventFactoryInterface);
        }
    }

    public void removeFactoryInterface(EventFactoryInterface eventFactoryInterface) {
        if(_interfaces.contains(eventFactoryInterface)) {
            _interfaces.remove(eventFactoryInterface);
        }
    }

    public EventAdapter getAdapter() { return _adapter; }
    public void setAdapter(EventAdapter value) {
        _adapter = value;
        updateShifts();
    }

    public void updateShifts() {

        LocalDate date = CalendarFactory.getInstance().getSelectedDate();
        final RealmResults<Event> eventResults = _realm.where(Event.class).contains("startTime", date.toString()).findAll().sort("startTime", Sort.ASCENDING);
        _adapter.updateEvents(eventResults);
        notifyShiftsUpdated(eventResults.size());
    }
    private void notifyShiftsUpdated(int results) {
        for(EventFactoryInterface cfInterface : _interfaces) {
            cfInterface.onShiftsUpdated(results);
        }
    }

    public ArrayList<Event> getEvents() { return _adapter.getEvents(); }
}
