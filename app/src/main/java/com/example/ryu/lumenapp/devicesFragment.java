package com.example.ryu.lumenapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class devicesFragment extends Fragment {

    private static SeekBar main_bar;
    private static TextView main_bar_text;
    private static Button update;
    private static int mainBarProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_devices, null);

        update = view.findViewById(R.id.update);
        main_bar = view.findViewById(R.id.mainBar);
        main_bar_text = view.findViewById(R.id.mainBarText);

        seekBar();
        update();
        return view;
    }

    public void seekBar() {

        main_bar_text.setText("Set to " + mainBarProgress + "% Open");



        main_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mainBarProgress = progress;
                main_bar_text.setText("Set to " + mainBarProgress + "% Open");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                main_bar_text.setText("Set to " + mainBarProgress + "% Open");
            }
        });
    }

    public void update() {

        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(devicesFragment.this.getActivity(), "Shades set to " +
                        mainBarProgress + "% Open", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void onResume(){
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity()).setActionBarTitle("Devices");

    }
}
