package com.peterblackburn.shiftmanager.Events.Interfaces;

import com.peterblackburn.shiftmanager.Events.Models.Event;

public interface EventInterface extends BaseInterface {
    void removeEvent(Event event);
}
