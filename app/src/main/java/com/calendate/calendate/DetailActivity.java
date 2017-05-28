package com.calendate.calendate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements DataSource.OnDataArrivedListener {

    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recycler = (RecyclerView) findViewById(R.id.recycler);
        DataSource.fetchItems(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = new Intent();
        intent.getIntExtra("btnId", 0);

    }

    @Override
    public void onDataArrived(ArrayList<Item> items, Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Adapter adapter = new Adapter(DetailActivity.this, DataSource.getItems());
                recycler.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
                recycler.setAdapter(adapter);
            }
        });
    }

}
