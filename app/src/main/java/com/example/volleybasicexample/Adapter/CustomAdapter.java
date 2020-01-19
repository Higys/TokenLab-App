package com.example.volleybasicexample.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.volleybasicexample.Controller.AppController;
import com.example.volleybasicexample.MainActivity;
import com.example.volleybasicexample.Model.Model;
import com.example.volleybasicexample.R;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List modelItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomAdapter(Activity activity, List modelItems){
        this.activity = activity;
        this.modelItems = modelItems;
    }

    @Override
    public int getCount() {
        return modelItems.size();
    }

    @Override
    public Object getItem(int position) {
        return modelItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.layout_films, null);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView title = convertView.findViewById(R.id.title);
        NetworkImageView thumbNail = convertView.findViewById(R.id.thumbnail);
        // getting model data for the row
        Model m = (Model) modelItems.get(position);
        title.setText(String.valueOf(m.getTitle()));
        thumbNail.setImageUrl(m.getThumbnailURL(), imageLoader);

        return convertView;
    }
}
