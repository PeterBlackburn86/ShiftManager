package com.peterblackburn.shiftmanager.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.peterblackburn.shiftmanager.R;

public class HomeFragment extends BaseFragment {

    private static HomeFragment _instance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);


        return v;
    }

    @Override
    public String getTitle() {
        return "Home";
    }

    public static HomeFragment getInstance() {
        if(_instance == null)
            _instance = new HomeFragment();

        return _instance;
    }

    public HomeFragment() {
    }
}
