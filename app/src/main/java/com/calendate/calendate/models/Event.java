package com.calendate.calendate.models;

import com.calendate.calendate.MyUtils;

import org.joda.time.LocalDateTime;


public class Event {

    String title;
    String description;
    String date;
    int alertCount;
    int alertKindPos;
    String time;
    int repeatPos;
    String eventUID;

    public Event() {

    }

    public Event(String title, String description, LocalDateTime date, int alertCount, int alertKindPos, int hours, int minutes, int repeatPos, String eventUID) {
        this.title = title;
        this.description = description;
        this.date = date.toString(MyUtils.dateFormat);
        this.alertCount = alertCount;
        this.alertKindPos = alertKindPos;
        this.time = hours + ":" + minutes;
        this.repeatPos = repeatPos;
        this.eventUID = eventUID;
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


    public String getTime() {
        return time;
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


    public void setTime(String time) {
        this.time = time;
    }

    public int getAlertCount() {
        return alertCount;
    }

    public void setAlertCount(int alertCount) {
        this.alertCount = alertCount;
    }

    public int getAlertKindPos() {
        return alertKindPos;
    }

    public void setAlertKindPos(int alertKindPos) {
        this.alertKindPos = alertKindPos;
    }

    public int getRepeatPos() {
        return repeatPos;
    }

    public void setRepeatPos(int repeatPos) {
        this.repeatPos = repeatPos;
    }

    public String getEventUID() {
        return eventUID;
    }

    public void setEventUID(String eventUID) {
        this.eventUID = eventUID;
    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", alertCount=" + alertCount +
                ", alertKindPos=" + alertKindPos +
                ", time='" + time + '\'' +
                ", repeatPos=" + repeatPos +
                ", eventUID='" + eventUID + '\'' +
                '}';
    }
}
