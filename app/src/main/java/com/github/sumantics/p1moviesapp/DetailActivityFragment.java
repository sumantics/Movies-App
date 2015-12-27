package com.github.sumantics.p1moviesapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivityFragment extends Fragment {
    static String LOGTAG = DetailActivityFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View detail = inflater.inflate(R.layout.fragment_detail, container, false);
        return populate(detail);
    }

    private View populate(View detail) {
        Movie movie = DetailActivity.movie;
        Log.d(LOGTAG, "onCreate " + movie);

        ImageView poster = (ImageView) detail.findViewById(R.id.poster_detail);
        TextView releaseDate = (TextView)detail.findViewById(R.id.release_date);
        TextView rating = (TextView)detail.findViewById(R.id.rating);
        TextView plot_synopsis = (TextView)detail.findViewById(R.id.plot_synopsis);

        if(movie!=null){
            poster.setImageResource(new Integer(movie.mPoster));
            releaseDate.setText(movie.mReleaseDate);
            rating.setText(movie.mVoteAvg);
            plot_synopsis.setText("Hello !!!!!!!!!!!!! "+movie.mplotSynopsis);
            Log.d(LOGTAG, "onCreate " + movie.mplotSynopsis);
        }
        return detail;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        populate(view);

    }
}
