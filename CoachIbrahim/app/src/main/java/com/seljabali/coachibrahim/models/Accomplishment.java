package com.seljabali.coachibrahim.models;

/**
 * Created by habibi on 12/7/14.
 */
public class Accomplishment {
    private String name;
    private int iconResourceID;

    public Accomplishment(String name) {
        this.name = name;
        this.iconResourceID = iconResourceID;
    }

    public Accomplishment(String name, int iconResourceID) {
        this.name = name;
        this.iconResourceID = iconResourceID;
    }

    public String getName() {
        return name;
    }

    public int getIconResourceID() {
        return iconResourceID;
    }
}
