package com.example.ryu.lumenapp;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class tasksFragment extends Fragment {
    Button button;
    private ListView listview;

    stringArrays strarrTitle = new stringArrays();
    stringArrays strarrDescription = new stringArrays();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, null);
        button = view.findViewById(R.id.addGroup);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),addTasks.class);
                startActivityForResult(intent,0);

            }
        });
        listview = view.findViewById(R.id.lvMain);
        setupListView();

        return view;
    }

    private void setupListView() {
        strarrTitle = strarrTitle.getInstance();
        if(strarrTitle == null) {
            return;
        }
        Log.d("hello", Integer.toString(strarrTitle.getStringArrays().size()));
        strarrDescription = strarrDescription.getInstance();
        if(strarrDescription == null) {
            return;
        }
        Log.d("hello", Integer.toString(strarrDescription.getStringArrays().size()));
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), strarrTitle, strarrDescription);
        listview.setAdapter(simpleAdapter);
    }

    public void onResume(){
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity()).setActionBarTitle("Tasks");

    }
    public class SimpleAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater layoutinflater;
        private TextView title,description;
        private stringArrays titleArray;
        private stringArrays descriptionArray;

        public SimpleAdapter(Context context, stringArrays title, stringArrays description){
            mContext = context;
            titleArray = title;
            descriptionArray = description;
        }
        @Override
        public int getCount() {
            return titleArray.getStringArrays().size();
        }

        @Override
        public Object getItem(int position) {
            return titleArray.getStringArrays().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override

        public View getView(int position, View convertView, ViewGroup parent) {
            title = (TextView)convertView.findViewById(R.id.tvMain);
            description = (TextView)convertView.findViewById(R.id.tvDescription);
            title.setText(titleArray.getStringArrays().get(position));
            description.setText(descriptionArray.getStringArrays().get(position));
            return convertView;
        }
    }
}
