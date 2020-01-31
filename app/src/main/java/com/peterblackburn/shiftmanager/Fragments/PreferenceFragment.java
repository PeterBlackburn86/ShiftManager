package com.peterblackburn.shiftmanager.Fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.peterblackburn.shiftmanager.R;

public class PreferenceFragment extends PreferenceFragmentCompat {

    public PreferenceFragment() {

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

}
