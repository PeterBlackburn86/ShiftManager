package com.peterblackburn.shiftmanager;

import com.peterblackburn.shiftmanager.Events.Models.Event;
import com.peterblackburn.shiftmanager.Events.Models.EventTemplate;

import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {

    public static final String EVENT_TABLE = "Event";
    public static final String TEMPLATE_TABLE = "EventTemplate";

    private static RealmHelper _instance;
    private Realm _realm;

    private RealmHelper() {
        _realm = Realm.getDefaultInstance();
    }

    public static RealmHelper getInstance() {
        if(_instance == null)
            _instance = new RealmHelper();

        return _instance;
    }

    public ArrayList<Event> addEvents(Event... events) {
        ArrayList<Event> eventResults = new ArrayList<>();
        _realm.beginTransaction();
        for(Event event : events) {
            eventResults.add(_realm.copyToRealm(event));
        }
        _realm.commitTransaction();
        return eventResults;
    }

    public ArrayList<EventTemplate> addEventTemplates(EventTemplate... templates) {
        ArrayList<EventTemplate> templateResults = new ArrayList<>();
        _realm.beginTransaction();
        for(EventTemplate template : templates) {
            templateResults.add(_realm.copyToRealm(template));
        }
        _realm.commitTransaction();
        return templateResults;
    }

    public void deleteTemplate(EventTemplate event) {
        _realm.beginTransaction();

        RealmResults<EventTemplate> results = _realm.where(EventTemplate.class)
                .equalTo("id", event.getId())
                .findAll();

        results.deleteAllFromRealm();
        _realm.commitTransaction();
    }

    public void deleteShift(Event event) {
        _realm.beginTransaction();

        RealmResults<Event> results = _realm.where(Event.class)
                .equalTo("id", event.getId())
                .findAll();

        results.deleteAllFromRealm();
        _realm.commitTransaction();
    }

    public long nextPrimaryKey(String className) {

        long _currentPrimaryKey = 0;
        if(className != null) {
            Realm realm = Realm.getDefaultInstance();
            Number key;
            switch (className) {
                case EVENT_TABLE:
                    key = realm.where(Event.class).max("id");
                    _currentPrimaryKey = (key == null) ? 0 : key.intValue() + 1;
                    break;
                case TEMPLATE_TABLE:
                    key = realm.where(EventTemplate.class).max("id");
                    _currentPrimaryKey = (key == null) ? 0 : key.intValue() + 1;
                    break;
            }
        }
        return _currentPrimaryKey;
    }
}
