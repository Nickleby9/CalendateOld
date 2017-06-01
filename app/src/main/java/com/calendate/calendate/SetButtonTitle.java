package com.calendate.calendate;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetButtonTitle extends DialogFragment {

    private OnTitleSetListener mListener;

    Button btnSet;
    EditText etTitle;

    public SetButtonTitle() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_button_title, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSet = (Button) view.findViewById(R.id.btnSet);
        etTitle = (EditText) view.findViewById(R.id.etTitle);

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etTitle.getText().toString();
                mListener.onTitleSet(s);
                dismiss();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTitleSetListener) {
            mListener = (OnTitleSetListener) context;
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


    public interface OnTitleSetListener {
        // TODO: Update argument type and name
        void onTitleSet(String title);
    }
}
