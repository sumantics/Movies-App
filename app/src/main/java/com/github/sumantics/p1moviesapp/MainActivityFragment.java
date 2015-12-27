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

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) { //called on rotation
        if(savedInstanceState!=null && savedInstanceState.containsKey("key")){
            movieList = savedInstanceState.getParcelableArrayList("key");
        }else{
            //movieList = new ArrayList<>(Arrays.asList(moviesArray));
            movieList = NetworkUtil.discover(getContext());
        }
        Log.d("onCreate",movieList.toString());
        super.onCreate(savedInstanceState);
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
        MovieAdapter movieAdapter = new MovieAdapter(getContext(), movieList);//should use getActivity()??
        gridView.setAdapter(movieAdapter);
        Log.d("MAFragment", "gridView:" + gridView.hashCode() + " " + gridView);
        Log.d("MAFragment", "movieAdapter:" + movieAdapter.hashCode() + " " + movieAdapter);
        return gridView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
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
