package com.calendate.calendate;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ButtonsFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private static final String BUTTON_ID = "button_id";
    private static final int PICK_IMAGE_REQUEST = 1;
    private OnFragmentInteractionListener mListener;
    private String buttonTitle;
    BootstrapButton btnTopLeft, btnTopRight, btnMiddleLeft, btnMiddleRight, btnBottomLeft, btnBottomRight;
    FirebaseUser user;
    FirebaseDatabase mDatabase;

    public ButtonsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_buttons, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();

        btnTopLeft = (BootstrapButton) view.findViewById(R.id.btnTopLeft);
        btnTopRight = (BootstrapButton) view.findViewById(R.id.btnTopRight);
        btnMiddleLeft = (BootstrapButton) view.findViewById(R.id.btnMiddleLeft);
        btnMiddleRight = (BootstrapButton) view.findViewById(R.id.btnMiddleRight);
        btnBottomLeft = (BootstrapButton) view.findViewById(R.id.btnBottomLeft);
        btnBottomRight = (BootstrapButton) view.findViewById(R.id.btnBottomRight);

        btnTopLeft.setBootstrapBrand(new CustomBootstrapStyle(view.getContext()));
        btnTopRight.setBootstrapBrand(new CustomBootstrapStyle(view.getContext()));
        btnMiddleLeft.setBootstrapBrand(new CustomBootstrapStyle(view.getContext()));
        btnMiddleRight.setBootstrapBrand(new CustomBootstrapStyle(view.getContext()));
        btnBottomLeft.setBootstrapBrand(new CustomBootstrapStyle(view.getContext()));
        btnBottomRight.setBootstrapBrand(new CustomBootstrapStyle(view.getContext()));


        btnTopLeft.setOnClickListener(this);
        btnTopRight.setOnClickListener(this);
        btnMiddleLeft.setOnClickListener(this);
        btnMiddleRight.setOnClickListener(this);
        btnBottomLeft.setOnClickListener(this);
        btnBottomRight.setOnClickListener(this);

        btnTopLeft.setOnLongClickListener(this);
        btnTopRight.setOnLongClickListener(this);
        btnMiddleLeft.setOnLongClickListener(this);
        btnMiddleRight.setOnLongClickListener(this);
        btnBottomLeft.setOnLongClickListener(this);
        btnBottomRight.setOnLongClickListener(this);

        readButtonTitle();
    }

    private void readButtonTitle() {

        String hex = MyUtils.fixEmail(user.getEmail());
        mDatabase.getReference("buttons/1/" + hex + "/" + btnTopLeft.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        btnTopLeft.setText(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        mDatabase.getReference("buttons/1/" + hex + "/" + btnTopRight.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        btnTopRight.setText(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        mDatabase.getReference("buttons/1/" + hex + "/" + btnMiddleLeft.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        btnMiddleLeft.setText(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        mDatabase.getReference("buttons/1/" + hex + "/" + btnMiddleRight.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        btnMiddleRight.setText(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        mDatabase.getReference("buttons/1/" + hex + "/" + btnBottomLeft.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        btnBottomLeft.setText(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        mDatabase.getReference("buttons/1/" + hex + "/" + btnBottomRight.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        btnBottomRight.setText(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public static ButtonsFragment newInstance() {
        Bundle args = new Bundle();

        ButtonsFragment fragment = new ButtonsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onButtonPressed(int btnId) {
        if (mListener != null) {
            mListener.onFragmentInteraction(btnId);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        onButtonPressed(id);
    }

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();
        showButtonOptionsDialog(v, id);
        return false;
    }

    public void showButtonOptionsDialog(final View v, final int btnId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle(R.string.change_button_dialog_title);
        builder.setItems(R.array.buttonOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //Change title
                        SetButtonTitleDialog f = new SetButtonTitleDialog();
                        Bundle args = new Bundle();
                        args.putInt(BUTTON_ID, btnId);
                        f.setArguments(args);
                        f.show(getFragmentManager(), "setButtonTitleDialog");
                        break;
                    case 1:
                        //Change image

                        break;
                    case 2:
                        //Delete - are you sure? -remove title and link to data
                        setButtonText((Button) v.findViewById(btnId), "");
                        break;
                }
            }
        });
        builder.show();
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int btnId);
    }

    public void setButtonText(final Button button, String text) {
        String hex = MyUtils.fixEmail(user.getEmail());
        mDatabase.getReference("buttons/1/" + hex + "/" + button.getId()).setValue(text);

        final DatabaseReference mRef = mDatabase.getReference("buttons/1/" + hex + "/" + button.getId());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                button.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
