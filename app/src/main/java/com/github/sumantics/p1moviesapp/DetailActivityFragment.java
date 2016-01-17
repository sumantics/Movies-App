package com.github.sumantics.p1moviesapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.sumantics.p1moviesapp.data.MovieContract;

public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    static String LOGTAG = DetailActivityFragment.class.getSimpleName();
    static TrailerAdapter mTrailerAdapter;
    static ReviewAdapter mReviewAdapter;
    static Uri mUri;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME+"."+MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RATING
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        if(getArguments()!=null)
            mUri = getArguments().getParcelable("movieDetailUri");
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
        //            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        return detail;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populate(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {//When detail activity is created(click on poster in potrait/land->potrait)
        if(savedInstanceState!=null && savedInstanceState.get("movieDetail")!=null) {
            DetailActivity.movie = (Movie)savedInstanceState.get("movieDetail");
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        if(this.getArguments()!=null && this.getArguments().getParcelable("movieDetail")!=null)
            DetailActivity.movie = this.getArguments().getParcelable("movieDetail");
        super.onAttach(context);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(mUri!=null)
        return new CursorLoader(
                getActivity(),
                mUri,
                MOVIE_COLUMNS,
                null,
                null,
                null
        );
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("DAF.onLoadFinished",data.toString());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d("DAF.onLoaderReset",loader.toString());
    }
}
