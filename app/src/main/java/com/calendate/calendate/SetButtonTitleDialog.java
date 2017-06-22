package com.calendate.calendate;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetButtonTitleDialog extends DialogFragment implements TextWatcher {

    private int btnId;
    private OnTitleSetListener mListener;
    BootstrapButton btnSet;
    EditText etTitle;
    TextView tvWarning;
    int fragNum;

    public SetButtonTitleDialog() {
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

        tvWarning = (TextView) view.findViewById(R.id.tvWarning);
        btnSet = (BootstrapButton) view.findViewById(R.id.btnSet);
        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etTitle.addTextChangedListener(this);
        MyUtils.fixBootstrapButton(view.getContext(), btnSet);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnId = getArguments().getInt("btnId");
        fragNum = getArguments().getInt("fragNum");

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etTitle.getText().toString();
                mListener.onTitleSet(s, btnId, fragNum);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 18) {
            tvWarning.setVisibility(View.VISIBLE);
            btnSet.setEnabled(false);
        } else {
            tvWarning.setVisibility(View.INVISIBLE);
            btnSet.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    public interface OnTitleSetListener {
        void onTitleSet(String title, int btnId, int fragNum);
    }
}
