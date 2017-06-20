package com.calendate.calendate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends AppCompatActivity {

    RecyclerView recycler;
    FirebaseDatabase mDatabase;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        recycler = (RecyclerView) findViewById(R.id.recycler);

        ItemsAdapter adapter = new ItemsAdapter(this, mDatabase.getReference("events/" + user.getUid()));
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddItem.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void readOnce() {

        mDatabase.getReference("events/" + user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot row : dataSnapshot.getChildren()) {
                    row.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    static class ItemsAdapter extends FirebaseRecyclerAdapter<Event, ItemsAdapter.ItemsViewHolder> {
        private Context context;
        public ItemsAdapter(Context context, Query query) {
            super(Event.class, R.layout.event_item, ItemsViewHolder.class, query);
            this.context = context;
        }

        @Override
        protected void populateViewHolder(ItemsViewHolder viewHolder, Event model, int position) {
            viewHolder.tvTitle.setText(model.getTitle());
            viewHolder.tvDate.setText(model.getDate());
            viewHolder.tvTitle.setHint(model.getKey());
        }

        public static class ItemsViewHolder extends RecyclerView.ViewHolder implements DialogInterface.OnClickListener {
            TextView tvTitle;
            TextView tvDate;

            public ItemsViewHolder(final View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
                tvDate = (TextView) itemView.findViewById(R.id.tvDate);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String key = tvTitle.getHint().toString();
                        Intent intent = new Intent(v.getContext(), DetailedItem.class);
                        intent.putExtra("key", key);
                        v.getContext().startActivity(intent);
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        deleteDialog();
                        return true;
                    }
                });

            }

            void deleteDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(itemView.getContext(), R.style.Theme_AppCompat_Dialog));
                builder.setTitle(R.string.delete_event)
                        .setMessage(R.string.confirm_delete)
                        .setPositiveButton(R.string.yes, this)
                        .setNegativeButton(R.string.no, this);
                AlertDialog dialog = builder.show();
            }

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String key = tvTitle.getHint().toString();
                    mDatabase.getReference("events/" + user.getUid()).child(key).removeValue();
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            }
        }
    }
}
