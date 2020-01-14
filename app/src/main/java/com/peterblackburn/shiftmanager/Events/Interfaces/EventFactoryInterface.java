package com.peterblackburn.shiftmanager.Events.Interfaces;

public interface EventFactoryInterface {
    void onShiftRemoved();
    void onShiftAdded();
    void onShiftsUpdated(int results);
}
