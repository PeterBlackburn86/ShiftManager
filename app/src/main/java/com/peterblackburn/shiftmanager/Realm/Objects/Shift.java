package com.peterblackburn.shiftmanager.Realm.Objects;

import org.threeten.bp.LocalDateTime;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Shift extends RealmObject {

    @PrimaryKey
    private long id;
    private String startTime;
    private String endTime;

    public Shift() {
        this(
                LocalDateTime.of(2020,1,1, 0, 0),
                LocalDateTime.of(2020,1,1, 0, 0)
        );
    }

    /**
     * Constructor with Start and End times for the shift.
     * @param startTime Sets the start date/time of the shift using LocalDateTime
     * @param endTime Sets the end date/time of the shift using LocalDateTime
     */
    public Shift(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime.toString();
        this.endTime = endTime.toString();

    }

    // Getters

    public long getId() { return id; }
    public LocalDateTime getStartTime() { return LocalDateTime.parse(startTime); }
    public LocalDateTime getEndTime() { return LocalDateTime.parse(endTime); }
//    public double getShiftPay() { return _shiftPay; }

    //Setters

    public void setId(long value) { id = value; }
    public void setStartTime(LocalDateTime value) { startTime = value.toString(); }
    public void setEndTime(LocalDateTime value) { endTime = value.toString(); }

    // Public Functions
    public void toPrettyString() {
        System.out.println("START: " + startTime + " | END: " + endTime + " | SHIFT HOURS: " + shiftHours());
    }

    public long shiftHours() {
        long hours = LocalDateTime.parse(startTime).compareTo(LocalDateTime.parse(endTime));
        System.out.println("HOURS: " + hours);
        return hours;
    }
}
