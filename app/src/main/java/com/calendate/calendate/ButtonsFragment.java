package com.calendate.calendate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ButtonsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ButtonsFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private static final String BUTTON_ID = "button_id";
    private static final int PICK_IMAGE_REQUEST = 1;
    private OnFragmentInteractionListener mListener;
    private String buttonTitle;
    Button btnTopLeft, btnTopRight, btnMiddleLeft, btnMiddleRight, btnBottomLeft, btnBottomRight;

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

        btnTopLeft = (Button) view.findViewById(R.id.btnTopLeft);
        btnTopRight = (Button) view.findViewById(R.id.btnTopRight);
        btnMiddleLeft = (Button) view.findViewById(R.id.btnMiddleLeft);
        btnMiddleRight = (Button) view.findViewById(R.id.btnMiddleRight);
        btnBottomLeft = (Button) view.findViewById(R.id.btnBottomLeft);
        btnBottomRight = (Button) view.findViewById(R.id.btnBottomRight);

        if (savedInstanceState != null) {
            buttonTitle = getArguments().getString("btn_title", "");
            btnTopLeft.setText(buttonTitle);
        }
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
        mListener.onFragmentInteraction(id);

//        switch (id) {
//            case R.id.btnTopLeft:
//                break;
//            case R.id.btnTopRight:
//
//                break;
//            case R.id.btnMiddleLeft:
//
//                break;
//            case R.id.btnMiddleRight:
//
//                break;
//            case R.id.btnBottomLeft:
//
//                break;
//            case R.id.btnBottomRight:
//
//                break;
//        }
    }

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();
        showButtonOptionsDialog(v, id);
        return false;
    }

    public void showButtonOptionsDialog(final View v, final int btnId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("What would you like to do?");
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
                        Intent intent = new Intent();
                        intent.setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select an icon"), PICK_IMAGE_REQUEST);
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

    public void setButtonText(Button button, String text){
        button.setText(text);
    }
}
