package com.peterblackburn.shiftmanager.Events.Enums;

public enum EventType {
    REGULAR_SHIFT ("regularShift", "Regular Shift"),
    OVERTIME ("overtimeShift", "Overtime Shift"),
    PAY_DAY ("payDay", "Pay Day");

    private String _realmName;
    private String _readadbleName;

    private EventType(String realmName, String readadbleName){
        _realmName = realmName;
        _readadbleName = readadbleName;
    }

    public String getRealmName() { return _realmName; }
    public String getReadableName() { return _readadbleName; }
    public static EventType getEventTypeFromName(String name) {
        for(EventType type : values()) {
            if(type._realmName.equals(name))
                return type;
        }
        return null;
    }

    @Override
    public String toString() {
        return _readadbleName;
    }
}
