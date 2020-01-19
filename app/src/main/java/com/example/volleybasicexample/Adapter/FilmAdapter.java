package com.example.volleybasicexample.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.volleybasicexample.Controller.AppController;
import com.example.volleybasicexample.Model.ModelFilm;
import com.example.volleybasicexample.R;

import java.util.List;

public class FilmAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List modelFilms;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public FilmAdapter(Activity activity, List modelFilms){
        this.activity = activity;
        this.modelFilms = modelFilms;
    }

    @Override
    public int getCount() {
        return modelFilms.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.layout_film, null);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();



        TextView title = convertView.findViewById(R.id.titleFilm);
        TextView tvGenres = convertView.findViewById(R.id.tv_Genre);
        TextView tvDescription = convertView.findViewById(R.id.tv_Overview);
        TextView tvStatus = convertView.findViewById(R.id.tv_statusText);
        TextView tvReleaseDate = convertView.findViewById(R.id.tv_ReleaseDate);
        NetworkImageView thumbnail = convertView.findViewById(R.id.thumbnail);
        ModelFilm model = (ModelFilm) modelFilms.get(position);
        RatingBar ratingBar = convertView.findViewById(R.id.ratingBar);

        title.setText(String.valueOf(model.getTitle()));
        tvGenres.setText(String.valueOf(model.getGenres()));


        ratingBar.setIsIndicator(true);
        ratingBar.setRating(Float.parseFloat(model.getRating())/2);

        tvStatus.setText(model.getStatus());
        tvReleaseDate.setText("(" + model.getReleaseDate() + ")");
        tvDescription.setText(model.getOverView());
        thumbnail.setImageUrl(model.getThumbnailURL(), imageLoader);

        return convertView;
    }

}
