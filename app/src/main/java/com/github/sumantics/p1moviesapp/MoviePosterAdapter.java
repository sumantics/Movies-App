package com.github.sumantics.p1moviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.sumantics.p1moviesapp.data.MovieContract;

import java.util.List;

public class MoviePosterAdapter extends ArrayAdapter<Movie> {
    public MoviePosterAdapter(Context context, List<Movie> objects) {
        super(context, 0, objects);
        this.setNotifyOnChange(true);
    }

    @Override
    public View getView(int position, View view, final ViewGroup parent) {
        if(view==null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie, parent, false);
        }

        final Movie movie = getItem(position);

        final ImageView poster = (ImageView) view.findViewById(R.id.grid_item_poster);  //use viewHolder, but since 1 item
        NetworkUtil.getPoster(getContext(), movie.mPoster, poster);
/*      working, but wanted to move this code to Fragment
        poster.setOnClickListener(new View.OnClickListener() { //does it belong here?
            @Override
            public void onClick(View view) {
                Log.d("Adapter onClick","parent"+parent.toString());
                Intent detailIntent = new Intent(view.getContext(),DetailActivity.class);
                detailIntent.putExtra("movieDetail", movie);
                view.getContext().startActivity(detailIntent);
            }
        });

*/
        //added checkbox for testing state-retention during rotation
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.grid_item_check);
        checkBox.setChecked(movie.mChecked);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movie.mChecked = !movie.mChecked;
                ContentValues values = new ContentValues();
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,movie.mMovieId);
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_ID,movie.mPoster);
                values.put(MovieContract.MovieEntry.COLUMN_RATING,movie.mVoteAvg);
                values.put(MovieContract.MovieEntry.COLUMN_TITLE,movie.mTitle);
                values.put(MovieContract.MovieEntry.COLUMN_REL_DATE,movie.mReleaseDate);
                values.put(MovieContract.MovieEntry.COLUMN_OVIEW,movie.mplotSynopsis);
                if(movie.mChecked){
                    try {
                        Uri insertedUri = getContext().getContentResolver().insert(
                                MovieContract.MovieEntry.buildMovieUri(Long.parseLong(movie.mMovieId)),
                                values
                        );
                        Toast.makeText(getContext().getApplicationContext(),"Fave'd movie "+movie.mTitle, Toast.LENGTH_SHORT);
                    }catch (android.database.SQLException ex){
                        Log.e("MoviePosterAdapter","Should be ok, if already inserted",ex);
                    }
                }else{
                    int numRowsDeleted = getContext().getContentResolver().delete(
                            MovieContract.MovieEntry.CONTENT_URI,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = "+movie.mMovieId,
                            new String[]{}
                    );
                    Toast.makeText(getContext().getApplicationContext(),"De-fave'd movie "+movie.mTitle, Toast.LENGTH_SHORT);
                    //Log.d("MoviePosterAdapter","numRowsDeleted:"+numRowsDeleted); //deleting fine, inserting fine : check why get is not returning anything
                }
            }
        });

        /* setChecked works fine when rotating, but fails during scroll
            the first 2 posters' check box state is recycled to the last 2 on scrolling(when first 2 become invisible).
            when a grid goes out of scope, the check=false event is sent
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                movie.mChecked = b;
                Log.d("checking", movie.mTitle + " : " + b+ " "+ movie.hashCode());
                somethingChanged();
            }
        });
        Note: Spent lot of time wondering why? Answer: the framework would uncheck the box to make it available for new occupant. Used onClick to solve this!
        */
        return view;
    }

}
