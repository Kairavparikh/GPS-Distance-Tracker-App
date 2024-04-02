package com.example.gpsproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<GPS> {
    List list;
    Context context;
    int xmlResource;
    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<GPS> objects) {
        super(context, resource, objects);
        xmlResource = resource;
        list = objects;
        this.context = context;
    }
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View adapterLayout = layoutInflater.inflate(xmlResource, null);
        TextView textView = adapterLayout.findViewById(R.id.textView);
        textView.setText(getItem(position).getAddress());
        TextView textView2 = adapterLayout.findViewById(R.id.textView2);
        textView2.setText(getItem(position).getTimeSpent());
        return adapterLayout;
    }

}

