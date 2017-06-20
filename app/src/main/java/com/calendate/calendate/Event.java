package com.calendate.calendate;

import org.joda.time.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Event {

    String title;
    String description;
    String date;
    int alertCount;
    String alertKind;
    String time;
    String repeat;
    String key;

    public Event(){

    }

    public Event(String title, String description, LocalDateTime date, int alertCount, String alertKind, String time, String repeat, String key) {
        this.title = title;
        this.description = description;
        this.date = date.toString("MMMM d, yyyy");
        this.alertCount = alertCount;
        this.alertKind = alertKind;
        this.time = time;
        this.repeat = repeat;
        this.key = key;
    }



    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public int getAlertCount() {
        return alertCount;
    }

    public String getAlertKind() {
        return alertKind;
    }

    public String getTime() {
        return time;
    }

    public String getRepeat() {
        return repeat;
    }

    public String getKey() {
        return key;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAlertCount(int alertCount) {
        this.alertCount = alertCount;
    }

    public void setAlertKind(String alertKind) {
        this.alertKind = alertKind;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
