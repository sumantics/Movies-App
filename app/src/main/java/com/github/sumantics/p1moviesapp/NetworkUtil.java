package com.github.sumantics.p1moviesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class NetworkUtil {
    static String LOGTAG = NetworkUtil.class.getSimpleName();
    static String apiKeyToken = "api_key=" + DoNotCommit.tmdb_movie_key;
    //"http://image.tmdb.org/t/p/w185/weUSwMdQIa3NaXVzwUoIIcAi85d.jpg"+apiKey

    static void discover(Context ctxt){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctxt);
        String choice = prefs.getString(ctxt.getString(R.string.pref_movieSort_key), ctxt.getString(R.string.pref_movieSort_popularity));
        //String choice = "popular";
        Log.d("NetworkUtil","selection is "+choice);
        if(choice.equals("rating")){
            VolleyGet(ctxt, "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&" + apiKeyToken);//need to have vote_count>x
        }
        VolleyGet(ctxt, "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&" + apiKeyToken);
    }
    private static void VolleyGet(final Context ctxt, String url){
        RequestQueue queue = Volley.newRequestQueue(ctxt);
        JsonObjectRequest request = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObj) {
                        Movie.parse(ctxt, jsonObj);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOGTAG, "Volley onErrorResponse:", error);
                Toast.makeText(ctxt,"Could not connect to the website. Are we online?", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }
    static void getPoster(Context context, String fileId, ImageView view){
        //reco size=w185
        String url = "http://image.tmdb.org/t/p/w185/"+fileId+"?"+apiKeyToken;
        Picasso.with(context).load(url).placeholder(R.drawable.starwars).error(R.drawable.starwars).into(view);
    }
    static void getReviews(final Context ctxt, final Movie movie){
        String url = "http://api.themoviedb.org/3/movie/"+movie.mMovieId+"/reviews?"+apiKeyToken;
        RequestQueue queue = Volley.newRequestQueue(ctxt);
        JsonObjectRequest request = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObj) {
                        movie.updateReviews(ctxt, jsonObj);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOGTAG+"getTrailer", "Volley onErrorResponse:", error);
            }
        });
        queue.add(request);
    }

    static void getTrailer(final Context ctxt, final Movie movie){
        String url = "http://api.themoviedb.org/3/movie/"+movie.mMovieId+"/videos?"+apiKeyToken;
        RequestQueue queue = Volley.newRequestQueue(ctxt);
        JsonObjectRequest request = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObj) {
                        movie.updateTrailers(ctxt, jsonObj);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOGTAG+"getTrailer", "Volley onErrorResponse:", error);
            }
        });
        queue.add(request);
    }
}
