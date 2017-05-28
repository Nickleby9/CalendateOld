package com.calendate.calendate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Item {

    private final String title;

    public Item(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
