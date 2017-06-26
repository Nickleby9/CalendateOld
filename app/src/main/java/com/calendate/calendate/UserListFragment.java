package com.calendate.calendate;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.calendate.calendate.models.Event;
import com.calendate.calendate.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserListFragment extends DialogFragment {

    RecyclerView rvUsers;
    FirebaseDatabase mDatabase;
    ArrayList<User> users = new ArrayList<>();
    FirebaseUser currentUser;
    String eventKey, btnRef;
    int fragNum;

    public UserListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        eventKey = getArguments().getString("key");
        btnRef = getArguments().getString("btnRef");
        fragNum = getArguments().getInt("fragNum");

        rvUsers = (RecyclerView) view.findViewById(R.id.rvUsers);
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        mDatabase.getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    users.add(user.getValue(User.class));
                    UserAdapter adapter = new UserAdapter(getContext(), users);
                    rvUsers.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class UserAdapter extends RecyclerView.Adapter<UserViewHolder>{

        ArrayList<User> data;
        LayoutInflater inflater;
        Context context;

        public UserAdapter(Context context, ArrayList<User> users) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            data = users;
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.user_item, parent, false);
            return new UserViewHolder(v);
        }

        @Override
        public void onBindViewHolder(UserViewHolder holder, int position) {
            User user = data.get(position);
            holder.tvUsername.setText(user.getUsername());
            holder.tvEmail.setText(user.getEmail());
            holder.tvUsername.setHint(user.getUid());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        TextView tvEmail;

        public UserViewHolder(View itemView) {
            super(itemView);

            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference ref = mDatabase.getReference("events/" + currentUser.getUid() + "/" + btnRef + fragNum);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot event : dataSnapshot.getChildren()) {
                                Event newEvent = event.getValue(Event.class);
                                if (event.getKey().equals(eventKey)){
                                    mDatabase.getReference("events/" + tvUsername.getHint().toString() + "/bottomRight2")
                                            .push().setValue(newEvent).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getContext(), "You shared the event", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    }
}
