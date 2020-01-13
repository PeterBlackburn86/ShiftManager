package com.peterblackburn.shiftmanager;

import android.app.Application;
import android.content.res.Resources;

import com.jakewharton.threetenabp.AndroidThreeTen;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ShiftApplication extends Application {

    private static ShiftApplication _instance;
    private static Resources _res;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        AndroidThreeTen.init(this);

        _instance = this;
        _res = getResources();
    }

    public static ShiftApplication getInstance() { return _instance; }
    public static Resources getRes() { return _res; }
}
