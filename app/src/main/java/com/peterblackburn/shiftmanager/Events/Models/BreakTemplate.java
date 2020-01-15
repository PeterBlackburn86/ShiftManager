package com.peterblackburn.shiftmanager.Events.Models;

import org.threeten.bp.LocalTime;

public class BreakTemplate {
    private String breakStart;
    private String breakEnd;
    private boolean isBreakPaid;

    public BreakTemplate() {
        this(
                true,
                LocalTime.now(),
                LocalTime.now()
        );
    }

    public BreakTemplate(boolean isPaid, LocalTime startTime, LocalTime endTime) {
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
