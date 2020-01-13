package com.peterblackburn.shiftmanager.Calendar.Interfaces;

import org.threeten.bp.LocalTime;

public interface TimeDateInterface {
    void onTimePicked(LocalTime time, String id);
}
