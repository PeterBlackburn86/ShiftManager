package com.peterblackburn.shiftmanager.Fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.peterblackburn.shiftmanager.R;

public class PreferenceFragment extends PreferenceFragmentCompat {

    private static PreferenceFragment _instance;

    public PreferenceFragment() {

    }

    public static PreferenceFragment getInstance() {
        if(_instance == null)
            _instance = new PreferenceFragment();
        return _instance;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

}
