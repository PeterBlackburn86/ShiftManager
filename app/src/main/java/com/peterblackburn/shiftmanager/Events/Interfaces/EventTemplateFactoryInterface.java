package com.peterblackburn.shiftmanager.Events.Interfaces;

public interface EventTemplateFactoryInterface {
    void onTemplateRemoved();
    void onTemplateAdded();
    void onTemplatesUpdated(int results);
}