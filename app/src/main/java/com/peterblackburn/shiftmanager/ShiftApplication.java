package com.peterblackburn.shiftmanager;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;

import androidx.preference.PreferenceManager;

import com.jakewharton.threetenabp.AndroidThreeTen;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ShiftApplication extends Application {

    private static ShiftApplication _instance;
    private Resources _res;
    private SharedPreferences prefs;

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
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public static ShiftApplication getInstance() { return _instance; }
    public Resources getRes() { return _res; }
    public boolean isDarkTheme() {
        return prefs.getBoolean("pref_dark_theme", false);
    }
}
