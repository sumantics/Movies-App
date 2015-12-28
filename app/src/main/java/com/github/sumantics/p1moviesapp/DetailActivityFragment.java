package com.github.sumantics.p1moviesapp;

import android.net.Uri;
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
        //View detail = getActivity().findViewById(R.id.fragment_detail);//trying to read already present child(from xml)
        return populate(detail);
    }

    private View populate(View detail) {
        Movie movie = DetailActivity.movie;
        Log.d(LOGTAG, "onCreate " + movie);

        TextView title = (TextView)detail.findViewById(R.id.title);
        ImageView poster = (ImageView) detail.findViewById(R.id.poster_detail);
        TextView releaseDate = (TextView)detail.findViewById(R.id.release_date);
        TextView rating = (TextView)detail.findViewById(R.id.rating);
        TextView plot_synopsis = (TextView)detail.findViewById(R.id.plot_synopsis);

        if(movie!=null){
            NetworkUtil.getPoster(getContext(), movie.mPoster, poster);
            title.setText(movie.mTitle);
            releaseDate.setText(movie.mReleaseDate);
            rating.setText(movie.mVoteAvg);
            plot_synopsis.setText(movie.mplotSynopsis);
        }
        return detail;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        populate(view);

    }
}
