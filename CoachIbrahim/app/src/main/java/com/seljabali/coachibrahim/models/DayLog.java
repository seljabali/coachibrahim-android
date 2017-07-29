package com.seljabali.coachibrahim.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import hirondelle.date4j.DateTime;

/**
 * Created by samsoom on 1/12/15.
 */
public class DayLog {
    private DateTime day;
    private boolean hasActivity;

    public DayLog() {
        super();
    }

    public DayLog(DateTime day, boolean hasActivity) {
        super();
        this.day = day;
        this.hasActivity = hasActivity;
    }

    public DayLog(Date date, boolean hasActivity) {
        super();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(date.getTime());
        this.day = new DateTime(formatted);
        this.hasActivity = hasActivity;
    }

    public void setHasActivity(boolean hasActivity) {
        this.hasActivity = hasActivity;
    }

    public void setDay(DateTime day) {
        this.day = day;
    }

    public boolean hasActivity() {
        return hasActivity;
    }

    public DateTime getDay() {
        return day;
    }

    @Override
    public String toString() {
        return "Day: " + day.toString() + ", hasActivity: " + hasActivity;
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof DayLog) {
            final DayLog other = (DayLog) o;
            return this.day.equals(other.day);
        }
        return false;
    }
    @Override
    public int hashCode() {
        return (int) day.getMilliseconds(new SimpleTimeZone(0, "GMT"));
    }
}
