package com.calendate.calendate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.calendate.calendate.models.Event;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DetailActivity extends AppCompatActivity {

    RecyclerView recycler;
    FirebaseDatabase mDatabase;
    FirebaseUser user;
    static String btnRef;
    static int fragNum;
    private static Context context;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("btnRef", btnRef);
        intent.putExtra("fragNum", fragNum);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnRef = getIntent().getStringExtra("btnRef");
        fragNum = getIntent().getIntExtra("fragNum", 0);

        mDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        recycler = (RecyclerView) findViewById(R.id.recycler);

        ItemsAdapter adapter = new ItemsAdapter(this, mDatabase.getReference("events/" + user.getUid() + "/" + btnRef + fragNum));
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddItem.class);
                intent.putExtra("btnRef", btnRef);
                intent.putExtra("fragNum", fragNum);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        public static class ItemsViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;
            TextView tvDate;

            public ItemsViewHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
                tvDate = (TextView) itemView.findViewById(R.id.tvDate);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailedItem.class);
                        intent.putExtra("key", tvTitle.getHint().toString());
                        intent.putExtra("btnRef", btnRef);
                        intent.putExtra("fragNum", fragNum);
                        v.getContext().startActivity(intent);
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        optionsDialog(v);
                        return true;
                    }
                });
            }

            void optionsDialog(final View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setTitle(R.string.change_button_dialog_title)
                        .setItems(R.array.itemOptions, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        //Share
                                        UserListFragment userListFragment = new UserListFragment();
                                        dialog.dismiss();
                                        if (v.getContext() instanceof FragmentActivity){
                                            Bundle bundle = new Bundle();
                                            bundle.putString("btnRef", btnRef);
                                            bundle.putInt("fragNum", fragNum);
                                            bundle.putString("key", tvTitle.getHint().toString());
                                            userListFragment.setArguments(bundle);
                                            userListFragment.show(((FragmentActivity) v.getContext()).getSupportFragmentManager(),"fragment");
                                        }
                                        break;
                                    case 1:
                                        //Delete
                                        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                                        builder.setTitle(R.string.confirm_delete)
                                                .setMessage(R.string.confirm_delete)
                                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                        mDatabase.getReference("events/" + user.getUid() + "/" + btnRef + fragNum + "/" + tvTitle.getHint()).removeValue();
                                                        dialogInterface.dismiss();
                                                    }
                                                })
                                                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                }).show();
                                        break;
                                }
                            }
                        }).show();
            }

        }
    }
}
