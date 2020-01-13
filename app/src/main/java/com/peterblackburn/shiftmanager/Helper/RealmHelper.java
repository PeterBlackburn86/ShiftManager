package com.peterblackburn.shiftmanager.Helper;

import com.peterblackburn.shiftmanager.Realm.Objects.Shift;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {

    private static RealmHelper _instance;
    private long _currentPrimaryKey = -1;
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

    public long nextPrimaryKey() {
        if(_currentPrimaryKey == -1) {
            Realm realm = Realm.getDefaultInstance();
            _currentPrimaryKey = realm.where(Shift.class).max("id").intValue() + 1;
            return _currentPrimaryKey;
        } else {
            _currentPrimaryKey += 1;
            return _currentPrimaryKey;
        }
    }
}
