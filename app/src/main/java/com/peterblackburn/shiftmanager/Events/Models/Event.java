package com.peterblackburn.shiftmanager.Events.Models;

import com.peterblackburn.shiftmanager.Events.Enums.EventType;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Event extends RealmObject {

    @PrimaryKey
    private long id;
    private boolean isAllDay;
    private String startTime;
    private String endTime;
    private String eventType;
    private boolean hasBreaks = false;
    private RealmList<Break> breaks;

    public Event() {
        this(
                false,
                LocalDateTime.of(2020,1,1, 0, 0),
                LocalDateTime.of(2020,1,1, 0, 0),
                EventType.REGULAR_SHIFT
        );
    }

    /**
     * Constructor with Start and End times for the shift.
     * @param startTime Sets the start date/time of the shift using LocalDateTime
     * @param endTime Sets the end date/time of the shift using LocalDateTime
     * @param eventType Sets the type of event from the EventType enum
     */
    public Event(boolean isAllDay, LocalDateTime startTime, LocalDateTime endTime, EventType eventType) {
        this.isAllDay = isAllDay;
        this.startTime = startTime.toString();
        this.endTime = endTime.toString();
        this.eventType = eventType.getRealmName();
        breaks = new RealmList<>();
    }

    // Getters
    public long getId() { return id; }
    public boolean isAllDay() { return isAllDay; }
    public LocalDateTime getStartTime() { return LocalDateTime.parse(startTime); }
    public LocalDateTime getEndTime() { return LocalDateTime.parse(endTime); }
    public EventType getEventType() {return EventType.getEventTypeFromName(eventType); }
    public boolean hasBreaks() { return hasBreaks; }
    public RealmList<Break> getBreaks() { return breaks;
    }

    //Setters
    public void setId(long value) { id = value; }
    public void setIsAllDay(boolean value) { isAllDay = value; }
    public void setStartTime(LocalDateTime value) { startTime = value.toString(); }
    public void setEndTime(LocalDateTime value) { endTime = value.toString(); }
    public void setEventType(EventType value) { eventType = value.getRealmName(); }
    public void setHasBreaks(boolean value) { hasBreaks = value; }

    // Public Functions
    public void toPrettyString() {
        System.out.println("******************** EVENT ***********************");
        if(isAllDay) {
            System.out.println("Event Type: " + getEventType().getReadableName() + " | Event Duration: All Day");
        } else  {
            System.out.println("Event Type: " + getEventType().getReadableName() + " | Event Start: " + startTime + " | Event End: " + endTime);
        }

        if(hasBreaks) {
            System.out.println("******************** BREAKS **********************");
            for(Break b : breaks) {
                System.out.println("Break Start: " + b.getBreakStart() + " | Break End: " + b.getBreakEnd() + " | Break Paid?: " + b.isBreakPaid());
            }
        }

        System.out.println("**************************************************");
        System.out.println("Total Shift Hours: " + shiftHours());
        System.out.println("Total Paid Hours: " + paidHours());
        System.out.println("**************************************************");
    }

    public long shiftHours() {
        Duration duration = Duration.between(getStartTime(), getEndTime());
        long hours = duration.toHours();
        return hours;
    }

    public long paidHours() {
        long hours = shiftHours();
        long unPaidBreaks = 0;
        for(Break b : breaks) {
            if(!b.isBreakPaid()) {
                Duration duration = Duration.between(b.getBreakStart(), b.getBreakEnd());
                long unPaidTime = duration.toHours();
                unPaidBreaks += unPaidTime;
            }
        }
        return hours - unPaidBreaks;
    }

    public void addBreak(Break eventBreak) {
        if(!breaks.contains(eventBreak)) {
            breaks.add(eventBreak);
        }
    }
    public void removeBreak(Break eventBreak) {
        if(breaks.contains(eventBreak)) {
        breaks.remove(eventBreak);
        }
    }

    //Private Functions
}
