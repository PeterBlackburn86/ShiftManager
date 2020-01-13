package com.peterblackburn.shiftmanager.Realm.Helper;

import com.peterblackburn.shiftmanager.Realm.Objects.Shift;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {

    public static final String SHIFT_TABLE = "Shift";

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

    public Realm getDefaultRealm() {
        return _realm;
    }

    public ArrayList<Shift> addShifts(Shift... shifts) {
        ArrayList<Shift> shiftResults = new ArrayList<>();
        _realm.beginTransaction();
        for(Shift shift : shifts) {
            shiftResults.add(_realm.copyToRealm(shift));
        }
        _realm.commitTransaction();
        return shiftResults;
    }

    public void deleteShift(Shift shift) {
        _realm.beginTransaction();

        RealmResults<Shift> results = _realm.where(Shift.class)
                .equalTo("id",shift.getId())
                .findAll();

        results.deleteAllFromRealm();
        _realm.commitTransaction();
    }

    public long nextPrimaryKey(String className) {

        long _currentPrimaryKey = -1;
        if(className != null) {
            Realm realm = Realm.getDefaultInstance();
            switch (className) {
                case SHIFT_TABLE:
                    _currentPrimaryKey = realm.where(Shift.class).max("id").intValue() + 1;
                    break;
            }
        }
        return _currentPrimaryKey;
    }
}
