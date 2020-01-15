package com.peterblackburn.shiftmanager.Events.Models;

import com.peterblackburn.shiftmanager.Events.Enums.EventType;

import org.threeten.bp.LocalDateTime;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EventTemplate extends RealmObject {

    @PrimaryKey
    private long id;
    private String templateName;
    private boolean isAllDay;
    private String startTime;
    private String endTime;
    private String eventType;
    private boolean hasBreaks = false;
    private RealmList<Break> breaks;

    public EventTemplate() {
        this(
                false,
                LocalDateTime.of(2020,1,1, 0, 0),
                LocalDateTime.of(2020,1,1, 0, 0),
                EventType.REGULAR_SHIFT
        );
    }

    /**
     * Constructor with Start and End times for the Event.
     * @param isAllDay Sets a boolean for if the event lasts all day.
     * @param startTime Sets the start date/time of the shift using LocalDateTime
     * @param endTime Sets the end date/time of the shift using LocalDateTime
     * @param eventType Sets the type of event from the EventType enum
     */
    public EventTemplate(boolean isAllDay, LocalDateTime startTime, LocalDateTime endTime, EventType eventType) {
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
    public RealmList<Break> getBreaks() { return breaks; }
    public String getTemplateName() { return templateName; }

    //Setters
    public void setId(long value) { id = value; }
    public void setIsAllDay(boolean value) { isAllDay = value; }
    public void setStartTime(LocalDateTime value) { startTime = value.toString(); }
    public void setEndTime(LocalDateTime value) { endTime = value.toString(); }
    public void setEventType(EventType value) { eventType = value.getRealmName(); }
    public void setHasBreaks(boolean value) { hasBreaks = value; }
    public void setTemplateName(String value) { templateName = value; }

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
}
