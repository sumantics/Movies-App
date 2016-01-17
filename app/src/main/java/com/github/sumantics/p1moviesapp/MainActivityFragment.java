package com.github.sumantics.p1moviesapp;

import android.content.ContentProvider;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.github.sumantics.p1moviesapp.data.MovieContract;

import java.util.ArrayList;
import com.github.sumantics.p1moviesapp.data.*;
/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    static ArrayList<Movie> movieList = new ArrayList<>();
    static String LOGTAG = MainActivityFragment.class.getSimpleName();
    //static MoviePosterAdapter moviePosterAdapter;
    static ListAdapter moviePosterAdapter;

    private static final String[] MOVIES_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME+"."+MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_ID,
            MovieContract.MovieEntry.COLUMN_TITLE
    };

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) { //called on rotation
        if(savedInstanceState!=null && savedInstanceState.containsKey("key")){
            movieList.clear();
            movieList = savedInstanceState.getParcelableArrayList("key");
            Log.d("onCreate:",movieList.toString());
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
        Log.d("onResume", movieList.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView)inflater.inflate(R.layout.fragment_main, container, false);
        //View relativeView = inflater.inflate(R.layout.fragment_main, container, false);
        //GridView gridView = (GridView)relativeView.findViewById(R.id.gridview_movies);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//does not come here if any of the children can be clicked
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //TODO : populate the data for deail view
                Log.d("MAF",adapterView.getItemAtPosition(position).toString());
                Movie movie = (Movie)adapterView.getItemAtPosition(position);//Poster I clicked??
                Log.d(LOGTAG, "clicked on " + movie.toString());
                ((Callback)getActivity()).onItemSelected(movie);//populate it later
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String choice = prefs.getString(getContext().getString(R.string.pref_movieSort_key), getContext().getString(R.string.pref_movieSort_popularity));
        //choice = getContext().getString(R.string.pref_movieSort_favourite);
        if(choice.equals(getContext().getString(R.string.pref_movieSort_favourite))){
            Cursor mCursor =  getContext().getContentResolver().query(
                    MovieContract.MovieEntry.favMoviesUri(),
                    new String[]{MovieContract.MovieEntry._ID,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_ID,
                            MovieContract.MovieEntry.COLUMN_TITLE,
                            MovieContract.MovieEntry.COLUMN_RATING,
                            MovieContract.MovieEntry.COLUMN_REL_DATE,
                            MovieContract.MovieEntry.COLUMN_OVIEW
                    },
                    null,
                    null,
                    null);
            moviePosterAdapter = new MovieCursorAdapter(getActivity(), mCursor, 0);
        }else{
            moviePosterAdapter = new MoviePosterAdapter(getContext(), movieList);
        }
        //moviePosterAdapter.setNotifyOnChange(true);
        gridView.setAdapter(moviePosterAdapter);
        return gridView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("onSaveInstanceState : ",movieList.toString());
        outState.putParcelableArrayList("key", movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = MovieContract.MovieEntry.COLUMN_TITLE + " ASC";//based on fav'd date??
        Uri uri = MovieContract.MovieEntry.favMoviesUri();

        return new CursorLoader(getActivity(),
                uri,
                MOVIES_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("MAF.onLoadFinished", data.toString());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d("MAF.onLoaderReset",loader.toString());
    }

    public interface Callback {
        public void onItemSelected(Movie movie);
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
