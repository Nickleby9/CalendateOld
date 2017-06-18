package com.calendate.calendate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hilay on 18-יוני-2017.
 */

public class Event {

    String title;
    String description;
    String c;
    int alertCount;
    String alertKind;
    String repeat;

    public Event(){

    }

    public Event(String title, String description, Calendar c, int alertCount, String alertKind, String repeat) {
        this.title = title;
        this.description = description;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        this.c  = dateFormat.format(new Date(c.getTimeInMillis()));
        this.alertCount = alertCount;
        this.alertKind = alertKind;
        this.repeat = repeat;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getC() {
        return c;
    }

    public int getAlertCount() {
        return alertCount;
    }

    public String getAlertKind() {
        return alertKind;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setC(String c) {
        this.c = c;
    }

    public void setAlertCount(int alertCount) {
        this.alertCount = alertCount;
    }

    public void setAlertKind(String alertKind) {
        this.alertKind = alertKind;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }
}
