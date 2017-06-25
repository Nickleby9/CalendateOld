package com.calendate.calendate;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimelineFragment extends Fragment {

    RecyclerView recycler;
    FirebaseDatabase mDatabase;
    FirebaseUser user;

    public TimelineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timelife, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        DatabaseReference ref = mDatabase.getReference("events/" + user.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                allEvents((Map<String, Event>), dataSnapshot.getValue());
//                https://stackoverflow.com/questions/38965731/how-to-get-all-childs-data-in-firebase-database
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ItemsAdapter adapter = new ItemsAdapter(getContext(), ref);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);

    }

    static class ItemsAdapter extends FirebaseRecyclerAdapter<Event, DetailActivity.ItemsAdapter.ItemsViewHolder> {
        private Context context;

        public ItemsAdapter(Context context, Query query) {
            super(Event.class, R.layout.event_item, DetailActivity.ItemsAdapter.ItemsViewHolder.class, query);
            this.context = context;
        }

        @Override
        protected void populateViewHolder(DetailActivity.ItemsAdapter.ItemsViewHolder viewHolder, Event model, int position) {
            viewHolder.tvTitle.setText(model.getTitle());
            viewHolder.tvDate.setText(model.getDate());
            viewHolder.tvTitle.setHint(model.getKey());
        }

        public static class ItemsViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;
            TextView tvDate;

            public ItemsViewHolder(final View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
                tvDate = (TextView) itemView.findViewById(R.id.tvDate);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailedItem.class);
                        intent.putExtra("key", tvTitle.getHint().toString());
                        v.getContext().startActivity(intent);
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        deleteDialog(v);
                        return true;
                    }
                });
            }

            void deleteDialog(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setTitle(R.string.delete_event)
                        .setMessage(R.string.confirm_delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                mDatabase.getReference("events/" + user.getUid() + "/" + tvTitle.getHint()).removeValue();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog dialog = builder.show();
            }
        }
    }
}
