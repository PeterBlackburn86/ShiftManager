package com.peterblackburn.shiftmanager.Events.Models;

import org.threeten.bp.LocalTime;

import io.realm.RealmObject;

public class Break extends RealmObject {

    private String breakStart;
    private String breakEnd;
    private boolean isBreakPaid;

    public Break() {
        this(
                true,
                LocalTime.now(),
                LocalTime.now()
        );
    }

    public Break(boolean isPaid, LocalTime startTime, LocalTime endTime) {
        this.isBreakPaid = isPaid;
        this.breakStart = startTime.toString();
        this.breakEnd = endTime.toString();
    }

    public LocalTime getBreakStart() { return LocalTime.parse(breakStart); }
    public LocalTime getBreakEnd() { return LocalTime.parse(breakEnd); }
    public boolean isBreakPaid() { return isBreakPaid; }

    public void setBreakStart(LocalTime value) { breakStart = value.toString(); }
    public void setBreakEnd(LocalTime value) { breakEnd = value.toString(); }
    public void setBreakPaid(boolean value) { isBreakPaid = value; }

}
