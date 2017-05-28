package com.calendate.calendate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailedItem extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_item);

        textView = (TextView) findViewById(R.id.textView);

        String title = getIntent().getStringExtra("title");

        textView.setText(title);
    }
}
