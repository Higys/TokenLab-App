package com.example.volleybasicexample;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.volleybasicexample.Adapter.FilmAdapter;
import com.example.volleybasicexample.Model.ModelFilm;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FilmActivity extends AppCompatActivity {

    private Toolbar myToolbarFilm;
    private String url;
    private int idFilm;
    Bundle extras;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private List modelFilmList;
    private FilmAdapter filmAdapter;
    private ListView listViewFilm;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);

        modelFilmList = new ArrayList();

        myToolbarFilm = findViewById(R.id.toolbar_film);
        setSupportActionBar(myToolbarFilm);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        extras = getIntent().getExtras();
        idFilm = Integer.parseInt(extras.getString("id_film"));

        listViewFilm = findViewById(R.id.viewFilm);
        filmAdapter = new FilmAdapter(FilmActivity.this, modelFilmList);
        listViewFilm.setAdapter(filmAdapter);

        mRequestQueue = Volley.newRequestQueue(this);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading..");
        pDialog.show();
        getFilmDetails();

    }

    void getFilmDetails(){
        url = "https://desafio-mobile.nyc3.digitaloceanspaces.com/movies/" + Integer.valueOf(idFilm).toString();

        final SharedPreferences sharedPreferences = getSharedPreferences("json_filmDetail_"+idFilm, Context.MODE_PRIVATE);

        if(loadFilmDetails() != null){ //if data is already in device do
            String responseFilmDetail = loadFilmDetails();
            pDialog.dismiss();
            try{
                ModelFilm modelFilm = new ModelFilm();
                JSONObject obj = new JSONObject(responseFilmDetail);

                modelFilm.setTitle(obj.getString("title"));
                modelFilm.setRating(obj.getString("vote_average"));
                modelFilm.setThumbnailURL(obj.getString("backdrop_url"));
                modelFilm.setOverView(obj.getString("overview"));
                modelFilm.setStatus(obj.getString("status"));
                modelFilm.setReleaseDate(obj.getString("release_date"));

                JSONArray a = obj.getJSONArray("genres");
                String genres = a.getString(0);
                for(int i=1; i<a.length(); i++){
                    genres += ", " + a.getString(i);
                }
                modelFilm.setGenres(genres);
                modelFilmList.add(modelFilm);
                filmAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
            filmAdapter.notifyDataSetChanged();
        }else{// if the device doesnt have any data
            if(MainActivity.isConnected(FilmActivity.this)){ //check internet connection
                mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {

                        pDialog.dismiss();
                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                        editor1.putString("film_detail_"+idFilm, response);
                        editor1.apply();

                        try{

                            ModelFilm modelFilm = new ModelFilm();
                            JSONObject obj = new JSONObject(response);

                            modelFilm.setTitle(obj.getString("title"));
                            modelFilm.setRating(obj.getString("vote_average"));
                            modelFilm.setThumbnailURL(obj.getString("backdrop_url"));
                            modelFilm.setOverView(obj.getString("overview"));
                            modelFilm.setStatus(obj.getString("status"));
                            modelFilm.setReleaseDate(obj.getString("release_date"));

                            //parse Genres Array
                            JSONArray a = obj.getJSONArray("genres");
                            String genres = a.getString(0);
                            for(int i=1; i<a.length(); i++){
                                genres += ", " + a.getString(i);
                            }
                            modelFilm.setGenres(genres);
                            modelFilmList.add(modelFilm);
                            filmAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        filmAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("FILMACTIVITY","Error :" + error.toString());
                    }
                });

                mRequestQueue.add(mStringRequest);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(FilmActivity.this);
                builder.setTitle(R.string.internetConnection);
                builder.setMessage(R.string.noInternetMessage)
                        .setPositiveButton(R.string.retryMessage, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getFilmDetails();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pDialog.dismiss();
                            }
                        });
                builder.show();

            }
        }


    }

    public String loadFilmDetails(){
        final SharedPreferences sharedPreferences = getSharedPreferences("json_filmDetail_"+idFilm, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("film_detail_"+idFilm, null);
        return json;
    }
}
