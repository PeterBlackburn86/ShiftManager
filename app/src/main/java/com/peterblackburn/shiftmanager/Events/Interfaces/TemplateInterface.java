package com.peterblackburn.shiftmanager.Events.Interfaces;

import com.peterblackburn.shiftmanager.Events.Models.EventTemplate;

public interface TemplateInterface extends BaseInterface {
    void removeTemplate(EventTemplate template);
}
