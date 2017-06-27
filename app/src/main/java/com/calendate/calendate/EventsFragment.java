package com.calendate.calendate;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calendate.calendate.models.EventRow;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_BTNREF = "btnRef";
    private static final String ARG_FRAGNUM = "fragNum";
    RecyclerView rvEvents;
    FloatingActionButton fabAddEvent;
    static String btnRef;
    static int fragNum;
    FirebaseUser user;

    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvEvents = (RecyclerView) view.findViewById(R.id.rvEvents);
        fabAddEvent = (FloatingActionButton) view.findViewById(R.id.fabAddEvent);
        fabAddEvent.setOnClickListener(this);
        btnRef = getArguments().getString(ARG_BTNREF);
        fragNum = getArguments().getInt(ARG_FRAGNUM);
        user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(user.getUid()).child(btnRef + fragNum);

        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEvents.setAdapter(new EventAdapter(ref, this));
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), AddItem.class);
        intent.putExtra(ARG_BTNREF, btnRef).putExtra(ARG_FRAGNUM, fragNum);
        startActivity(intent);
    }

    static class EventAdapter extends FirebaseRecyclerAdapter<EventRow, EventAdapter.EventViewHolder> {
        Fragment fragment;

        public EventAdapter(Query query, Fragment fragment) {
            super(EventRow.class, R.layout.event_item, EventAdapter.EventViewHolder.class, query);
            this.fragment = fragment;
        }

        @Override
        public EventAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new EventAdapter.EventViewHolder(v, fragment);
        }

        @Override
        protected void populateViewHolder(EventAdapter.EventViewHolder viewHolder, EventRow model, int position) {
            viewHolder.tvTitle.setText(model.getTitle());
            viewHolder.tvDate.setText(model.getDate());
            viewHolder.model = model;
        }

        public static class EventViewHolder extends RecyclerView.ViewHolder {
            private static final String ARG_EVENT = "model";
            TextView tvTitle;
            TextView tvDate;
            EventRow model;
            Fragment fragment;


            public EventViewHolder(View itemView, final Fragment fragment) {
                super(itemView);
                this.fragment = fragment;
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
                tvDate = (TextView) itemView.findViewById(R.id.tvDate);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(v.getContext(), DetailedItem.class);
//                        intent.putExtra("btnRef", btnRef);
//                        intent.putExtra("fragNum", fragNum);
//                        v.getContext().startActivity(intent);

                        EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(ARG_EVENT, model);
                        eventDetailsFragment.setArguments(bundle);
                        fragment.getFragmentManager().beginTransaction().replace(R.id.frame, eventDetailsFragment).commit();
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
                                        if (v.getContext() instanceof FragmentActivity) {
                                            Bundle bundle = new Bundle();
                                            bundle.putParcelable(ARG_EVENT, model);
                                            userListFragment.setArguments(bundle);
                                            userListFragment.show(((FragmentActivity) v.getContext()).getSupportFragmentManager(), "fragment");
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
                                                        mDatabase.getReference("events/" + user.getUid() + "/" + btnRef + fragNum + "/" + model.getEventUID()).removeValue();
                                                        mDatabase.getReference("all_events/" + user.getUid() + "/" + model.getEventUID()).removeValue();
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
