package com.github.sumantics.p1moviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.sumantics.p1moviesapp.data.MovieContract;

import java.util.Arrays;

public class MovieCursorAdapter extends CursorAdapter {
    LayoutInflater cursorInflater;

    public MovieCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("MCA:constr",c.getColumnCount()+" "+c.getCount());
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        /*
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_movie, parent, false);
        bindView(view, context, cursor);
        Log.d("MCA", "newView");
        return view;
        */
        return cursorInflater.inflate(R.layout.grid_item_movie, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ImageView poster = (ImageView) view.findViewById(R.id.grid_item_poster);  //use viewHolder, but since 1 item
        String mId = cursor.getString(0);
        final Movie mMovie = new Movie(cursor.getString(3), cursor.getString(1), cursor.getString(2), cursor.getString(5), cursor.getString(4), cursor.getString(6), true);
        poster.setImageResource(R.drawable.starwars);
        //TODO get poster from res/db
        NetworkUtil.getPoster(context, mMovie.mPoster, poster);

        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.grid_item_check);
        checkBox.setChecked(true);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MCA:onClick", "deleting movieId:" + mMovie.mMovieId + " checked:"+checkBox.isChecked());
                if(checkBox.isChecked()){
                    ContentValues values = new ContentValues();
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.mMovieId);
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_ID, mMovie.mPoster);
                    values.put(MovieContract.MovieEntry.COLUMN_RATING, mMovie.mVoteAvg);
                    values.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.mTitle);
                    values.put(MovieContract.MovieEntry.COLUMN_REL_DATE, mMovie.mReleaseDate);
                    values.put(MovieContract.MovieEntry.COLUMN_OVIEW, mMovie.mplotSynopsis);
                    Uri insertedUri = context.getContentResolver().insert(
                            MovieContract.MovieEntry.buildMovieUri(Long.parseLong(mMovie.mMovieId)),
                            values
                    );
                }else {
                    context.getContentResolver().delete(
                            MovieContract.MovieEntry.CONTENT_URI,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{mMovie.mMovieId}
                    );
                    view.invalidate();
                }
            }
        });
    }
}
