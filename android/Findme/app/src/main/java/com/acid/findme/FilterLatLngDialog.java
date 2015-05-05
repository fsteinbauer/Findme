package com.acid.findme;

import com.acid.findme.R;
import com.acid.findme.RangeSeekBar.OnRangeSeekBarChangeListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FilterLatLngDialog extends DialogFragment {

    public interface FilterLatLngDialogListener {
        public void onDialogPositiveClick(FilterLatLngDialog dialog);
        public void onDialogNegativeClick(FilterLatLngDialog dialog);
    }

    FilterLatLngDialogListener mListener;

    protected RangeSeekBar<Float> latSeekBar, lngSeekBar;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (FilterLatLngDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DefaultDialog);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();


        // Create the Layout
        latSeekBar = new RangeSeekBar<Float>(46f, 49f, getActivity());
        lngSeekBar = new RangeSeekBar<Float>(9f, 17.6f, getActivity());

        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.dialog_filterlatlng, null);

        LinearLayout latLayout = (LinearLayout) linearLayout.findViewById(R.id.latLinearLayout);
        latLayout.addView(latSeekBar);

        LinearLayout lngLayout = (LinearLayout) linearLayout.findViewById(R.id.lngLinearLayout);
        lngLayout.addView(lngSeekBar);

        final TextView tvLat = (TextView) linearLayout.findViewById(R.id.tvLat);
        tvLat.setText(latSeekBar.getAbsoluteMinValue()+"-"+latSeekBar.getAbsoluteMaxValue());
        final TextView tvLng = (TextView) linearLayout.findViewById(R.id.tvLng);
        tvLng.setText(lngSeekBar.getAbsoluteMinValue()+"-"+lngSeekBar.getAbsoluteMaxValue());


        latSeekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Float>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Float minValue, Float maxValue) {
                tvLat.setText(minValue + "-" + maxValue);
            }
        });

        lngSeekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Float>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Float minValue, Float maxValue) {
                tvLng.setText(minValue + "-" + maxValue);
            }
        });



        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.DefaultDialog));
        builder.setView(linearLayout)
                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(FilterLatLngDialog.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(FilterLatLngDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}