package com.example.appfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appfood.R;

import java.util.ArrayList;

public class PredictionAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> arrayList;
    int layoutResource;

    public PredictionAdapter(Context context, int layoutResource, ArrayList<String> objects){
        super(context, layoutResource, objects);
        this.context = context;
        this.layoutResource = layoutResource;
        this.arrayList = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(layoutResource,null);
        TextView txtHint = (TextView)convertView.findViewById(R.id.txtHint);
        txtHint.setText(arrayList.get(position));
        return convertView;
    }
}
