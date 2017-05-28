package com.calendate.calendate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ButtonsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ButtonsFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    ImageButton btnTopLeft, btnTopRight, btnMiddleLeft, btnMiddleRight, btnBottomLeft, btnBottomRight;

    public ButtonsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buttons, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnTopLeft = (ImageButton) view.findViewById(R.id.btnTopLeft);
        btnTopRight = (ImageButton) view.findViewById(R.id.btnTopRight);
        btnMiddleLeft = (ImageButton) view.findViewById(R.id.btnMiddleLeft);
        btnMiddleRight = (ImageButton) view.findViewById(R.id.btnMiddleRight);
        btnBottomLeft = (ImageButton) view.findViewById(R.id.btnBottomLeft);
        btnBottomRight = (ImageButton) view.findViewById(R.id.btnBottomRight);

        btnTopLeft.setOnClickListener(this);
        btnTopRight.setOnClickListener(this);
        btnMiddleLeft.setOnClickListener(this);
        btnMiddleRight.setOnClickListener(this);
        btnBottomLeft.setOnClickListener(this);
        btnBottomRight.setOnClickListener(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        switch (id){
            case R.id.btnTopLeft:
                mListener.onFragmentInteraction(R.id.btnTopLeft);
                break;
            case R.id.btnTopRight:

                break;
            case R.id.btnMiddleLeft:

                break;
            case R.id.btnMiddleRight:

                break;
            case R.id.btnBottomLeft:

                break;
            case R.id.btnBottomRight:

                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int btnId);
    }
}
