package com.example.volleybasicexample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.volleybasicexample.Adapter.CustomAdapter;
import com.example.volleybasicexample.Controller.AppController;
import com.example.volleybasicexample.Model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String url ="https://desafio-mobile.nyc3.digitaloceanspaces.com/movies";

    private ProgressDialog pDialog;

    private List modelList = new ArrayList();
    private ListView listView;
    private CustomAdapter adapter;
    Toolbar myToolbar;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        listView = findViewById(R.id.list);
        adapter = new CustomAdapter(this, modelList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading..");
        pDialog.show();
        getFilms();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Model modelAux = (Model) modelList.get(position);
                int idFilm = modelAux.getId();
                String idFilm2 = String.valueOf(idFilm);
                intent = new Intent(MainActivity.this, FilmActivity.class);
                intent.putExtra("id_film", idFilm2);
                startActivity(intent);
            }
        });

    }

    private String loadFilms() {
        final SharedPreferences sharedPreferences = getSharedPreferences("json_films", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("films_list", null);
        return json;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        getFilms();
    }


    private void getFilms(){
        final SharedPreferences sharedPreferences = getSharedPreferences("json_films", Context.MODE_PRIVATE);
        // Is data recorded?
        if(loadFilms() != null){
            //Convert the String Array returned by method loadFilms to JSONArray
            String responseString = loadFilms();
            pDialog.dismiss();
            JSONArray responseData = null;
            try {
                responseData = new JSONArray(responseString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            modelList.clear();
            //Parse the JSONArray and set the adapter
            for (int i = 0; i < responseData.length(); i++) {
                try {
                    JSONObject obj = responseData.getJSONObject(i);
                    Model model = new Model();
                    model.setTitle(obj.getString("title"));
                    model.setId(obj.getInt("id"));
                    model.setThumbnailURL(obj.getString("poster_url"));
                    modelList.add(model);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.e("Tamanho Lista", Integer.toString(modelList.size()));
            // notifying list adapter about data changes so that it renders the list view with updated data
            adapter.notifyDataSetChanged();


        }else{//If doesnt have any data stored, do
            if(isConnected(getApplicationContext())){ //check the internet connection
                // Showing progress dialog before making http request
                // Creating volley request obj
                final JsonArrayRequest movieReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pDialog.dismiss();
                        //save JSON in device
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String json = response.toString();
                        editor.putString("films_list", json);
                        editor.apply();

                        //parsing JSON
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Model model = new Model();
                                model.setTitle(obj.getString("title"));
                                model.setId(obj.getInt("id"));
                                model.setThumbnailURL(obj.getString("poster_url"));

                                modelList.add(model);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // notifying list adapter about data changes so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        pDialog.dismiss();

                    }
                });
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(movieReq);

            }else {//Set an Alert Dialog to close app or retry connection if device is not connected in internet
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.internetConnection);
                builder.setMessage(R.string.noInternetMessage)
                        .setPositiveButton(R.string.retryMessage, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getFilms();
                            }
                        })
                        .setNegativeButton(R.string.destroyApp, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onDestroy();
                            }
                        });
                builder.show();

            }

        }
    }

    public static boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( cm != null ) {
            NetworkInfo ni = cm.getActiveNetworkInfo();

            if(ni != null && ni.isConnected()){
                return true;
            };
        }

        return false;
    }
}


