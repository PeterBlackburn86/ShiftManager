package com.peterblackburn.shiftmanager.Theme.Model;

import io.realm.RealmObject;

public class ThemeValue extends RealmObject {

    private long id;
    private long themeId;
    private String controlId;
    private String key;
    private String value;
}
