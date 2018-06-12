package com.example.ryu.lumenapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.InputStream;


public class devicesFragment extends Fragment {

    private static SeekBar main_bar;
    private static TextView main_bar_text;
    private static Button update;
    private static int mainBarProgress;
    private final String SERVER_IP = "192.168.0.126";
    private final int SERVER_PORT = 22;
    private final String USER = "pi";
    private final String PW = "ece140a";
    private final int closeMax = 1156;
    private final int openMax = 1176;
    private int currPosition = 0;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        View view = inflater.inflate(R.layout.fragment_devices, null);

        update = view.findViewById(R.id.update);
        main_bar = view.findViewById(R.id.mainBar);
        main_bar_text = view.findViewById(R.id.mainBarText);
        main_bar.setProgress(mainBarProgress);

        seekBar();
        update();
        return view;
    }

    public void seekBar() {

        main_bar_text.setText("Set to " + mainBarProgress + "% Open");



        main_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               // mainBarProgress = progress;

               // main_bar_text.setText("Set to " + mainBarProgress + "% Open");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //prev_mainBarProgress = main_bar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mainBarProgress = (main_bar.getProgress()/10)*10;
                Log.d("hello?", Integer.toString(mainBarProgress));
                main_bar.setProgress(mainBarProgress);
                main_bar_text.setText("Set to " + mainBarProgress + "% Open");
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void update() {
        new AsyncTask<Integer, Void, Void>() {
            protected Void doInBackground(Integer... params) {
                try {
                    update.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(devicesFragment.this.getActivity(), "Shades set to " +
                                    mainBarProgress + "% Open", Toast.LENGTH_SHORT).show();
                            if (main_bar.getProgress() > currPosition) {
                                double differenceOpen = (main_bar.getProgress() - currPosition);
                                differenceOpen = (differenceOpen/100)* openMax;
                                int diffOpen = (int) differenceOpen;

                                try {
                                    runSSHCommandOpen(diffOpen);
                                    currPosition = main_bar.getProgress();
                                    SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putInt("currPosition",currPosition);
                                    editor.commit();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else if (main_bar.getProgress() < currPosition) {
                                double differenceClose = Math.abs(mainBarProgress - currPosition);
                                differenceClose = (differenceClose / 100) * closeMax;
                                int diffClose = (int) differenceClose;

                                try {
                                    runSSHCommandClose(diffClose);
                                    currPosition = main_bar.getProgress();
                                } catch (Exception e) {
                                    Log.d("close exception", "close exception");
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                }
                catch(Exception e) {
                    Log.d("hello", "hello world");
                    e.printStackTrace();
                }
            return null;
            }
        }.execute(1);
    }

    public void onResume(){
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity()).setActionBarTitle("Devices");

    }

    public void runSSHCommandOpen(int steps) {
        JSch jsch = new JSch();
        try {
            jsch.setConfig("StrictHostKeyChecking", "no");
            Session session = jsch.getSession(USER,SERVER_IP,SERVER_PORT);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(PW);
            session.connect(1000);
            Log.d("a","session is alive:" + session.isConnected());
            ChannelExec ch = (ChannelExec)session.openChannel("exec");
            String cmd = "python ./LumenIOT/open.py" + " " + Integer.toString(steps);
            ch.setCommand(cmd);
            ch.connect();

            ch.disconnect();
        }
        catch(JSchException jse) {
            jse.printStackTrace();
        }
    }

    public void runSSHCommandClose(int steps) {
        JSch jsch = new JSch();
        try {
            jsch.setConfig("StrictHostKeyChecking", "no");
            Session session = jsch.getSession(USER,SERVER_IP,SERVER_PORT);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(PW);
            session.connect(1000);
            Log.d("a","session is alive:" + session.isConnected());
            ChannelExec ch = (ChannelExec)session.openChannel("exec");
            String cmd = "python ./LumenIOT/close.py" + " " + Integer.toString(steps);
            ch.setCommand(cmd);
            ch.connect();

            ch.disconnect();
        }
        catch(JSchException jse) {
            jse.printStackTrace();
        }
    }
}
