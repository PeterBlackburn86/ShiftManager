package com.peterblackburn.shiftmanager.Theme.Model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Theme extends RealmObject {

    private long id;
    private long userId;
    private String themeName;
    private String themeDescription;
    private RealmList<ThemeValue> values = new RealmList<>();

}
