package com.github.sumantics.p1moviesapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    static ArrayList<Movie> movieList = new ArrayList<>();
    static String LOGTAG = MainActivityFragment.class.getSimpleName();
    static MovieAdapter movieAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) { //called on rotation
        if(savedInstanceState!=null && savedInstanceState.containsKey("key")){
            movieList.clear();
            movieList = savedInstanceState.getParcelableArrayList("key");
            Log.d("onCreate",movieList.toString());
        }else{
            NetworkUtil.discover(getContext());
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume", movieList.toString());
        NetworkUtil.discover(getContext());
        //movieAdapter.clear();
        Log.d("onResume", movieList.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView)inflater.inflate(R.layout.fragment_main, container, false);
        //View relativeView = inflater.inflate(R.layout.fragment_main, container, false);
        //GridView gridView = (GridView)relativeView.findViewById(R.id.gridview_movies);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//does not come here
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Object movie = adapterView.getItemAtPosition(position);//Poster I clicked??
                Toast.makeText(getActivity(), "clicked " + movie.toString(), Toast.LENGTH_SHORT).show();
                Log.d(LOGTAG, "clicked on " + movie.toString());
                Intent detailIntent = new Intent(getActivity(),DetailActivityFragment.class);
                getActivity().startActivity(detailIntent);
            }
        });
        movieAdapter = new MovieAdapter(getContext(), movieList);
        //movieAdapter.setNotifyOnChange(true);
        gridView.setAdapter(movieAdapter);
        return gridView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("onSaveInstanceState : ",movieList.toString());
        outState.putParcelableArrayList("key", movieList);
        super.onSaveInstanceState(outState);
    }

    /*
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {//not called?
        //super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null && savedInstanceState.containsKey("key")) {
            Log.d(LOGTAG, "In Restore "+ savedInstanceState.getParcelableArray("key").toString());
        }
    }
    */


}
