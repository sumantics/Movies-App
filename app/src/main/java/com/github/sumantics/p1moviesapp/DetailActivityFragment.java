package com.github.sumantics.p1moviesapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DetailActivityFragment extends Fragment {
    static String LOGTAG = DetailActivityFragment.class.getSimpleName();
    static TrailerAdapter mTrailerAdapter;
    static ReviewAdapter mReviewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View detail = inflater.inflate(R.layout.fragment_detail, container, false);
        //View detail = getActivity().findViewById(R.id.fragment_detail);//trying to read already present child(from xml)
        return populate(detail);
    }

    private View populate(View detail) {
        Movie movie = DetailActivity.movie;
        Log.d(LOGTAG, "onCreate:" + movie);

        TextView title = (TextView)detail.findViewById(R.id.title);
        ImageView poster = (ImageView) detail.findViewById(R.id.poster_detail);
        TextView releaseDate = (TextView)detail.findViewById(R.id.release_date);
        TextView rating = (TextView)detail.findViewById(R.id.rating);
        TextView plot_synopsis = (TextView)detail.findViewById(R.id.plot_synopsis);
        ListView trailerHolder = (ListView)detail.findViewById(R.id.trailers_holder);
        ListView reviewsHolder = (ListView)detail.findViewById(R.id.reviews_holder);

        if(movie!=null){
            NetworkUtil.getPoster(getContext(), movie.mPoster, poster);
            title.setText(movie.mTitle);
            releaseDate.setText(movie.mReleaseDate);
            rating.setText(movie.mVoteAvg);
            plot_synopsis.setText(movie.mplotSynopsis);

            mTrailerAdapter = new TrailerAdapter(getContext(), movie.trailers);
            trailerHolder.setAdapter(mTrailerAdapter);

            mReviewAdapter = new ReviewAdapter(getContext(), movie.reviews);
            reviewsHolder.setAdapter(mReviewAdapter);
        }

        movie.backgroundGet(getContext());
        return detail;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populate(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if(savedInstanceState!=null)
        Log.d("Sumanth",savedInstanceState.getString("movieDetail"));
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        Log.d("Sumanth::",context.toString()+" :: "+DetailActivity.movie);
        super.onAttach(context);
    }
}
