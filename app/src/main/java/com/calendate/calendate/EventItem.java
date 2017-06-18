package com.calendate.calendate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class EventItem {

    private final String title;
    private final String date;

    public EventItem(String title, String date) {
        this.title = title;
        this.date = date;
    }

}
