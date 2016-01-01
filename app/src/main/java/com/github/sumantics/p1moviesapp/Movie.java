package com.github.sumantics.p1moviesapp;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Parcelable{
    String mTitle;
    String mMovieId;
    String mPoster;
    String mReleaseDate;
    String mVoteAvg;
    String mplotSynopsis;
    boolean mChecked;
    List<Trailer> trailers = new ArrayList<>();
    List<Review> reviews = new ArrayList<>();

    public Movie(String name, String id, String poster, String releaseDate, String voteAvg, String overview){
        this(name, id, poster, releaseDate, voteAvg, overview, false);
    }
    public Movie(String name, String id, String poster, String releaseDate, String voteAvg, String overview, boolean checked){
        mTitle = name;
        mMovieId = id;
        mPoster = poster;
        mReleaseDate = releaseDate;
        mVoteAvg = voteAvg;
        mplotSynopsis = overview;
        mChecked = checked;
    }

    @Override
    public String toString() {
        return mTitle +":"+mChecked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mMovieId);
        parcel.writeString(mPoster);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mVoteAvg);
        parcel.writeString(mplotSynopsis);
        parcel.writeBooleanArray(new boolean[]{mChecked});
    }

    public static Creator<Movie> CREATOR = new Creator<Movie>(){
        @Override
        public Movie createFromParcel(Parcel parcel) {
            boolean [] boolArr = new boolean[]{true};
            //parcel.readBooleanArray(boolArr);//commenting for now... error during detail view load
            return new Movie(parcel.readString(),parcel.readString(),parcel.readString(),parcel.readString(),parcel.readString(),parcel.readString(), boolArr[0]);
        }

        @Override
        public Movie[] newArray(int var1){
            return new Movie[var1];
        }
    };

    static void parse(Context ctxt, JSONObject moviesJson){
        if(moviesJson.has("results")){
            MainActivityFragment.movieList.clear();
            try {
                JSONArray results = moviesJson.getJSONArray("results");
                for(int i=0; i<results.length(); i++){
                    JSONObject result = results.getJSONObject(i);
                    result.getString("poster_path");
                    Movie movie = new Movie(result.getString("title"),result.getString("id"),result.getString("poster_path"),result.getString("release_date"),result.getString("vote_average"),result.getString("overview"));
                    MainActivityFragment.movieList.add(movie);
                }
            }catch(JSONException jsonE){
                Log.e("Movie::parse", "Error during parsing", jsonE);
            }
            MainActivityFragment.moviePosterAdapter.notifyDataSetInvalidated();
            Log.d("parse", MainActivityFragment.movieList.toString());
            Log.d("parse",MainActivityFragment.moviePosterAdapter.getItem(1).toString());
        }
    }

    public void updateReviews(Context ctxt, JSONObject reviewsJson) {
        if (reviewsJson.has("results")) {
            DetailActivityFragment.mReviewAdapter.clear();
            try {
                JSONArray results = reviewsJson.getJSONArray("results");
                for(int i=0; i<results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    DetailActivityFragment.mReviewAdapter.add(new Review(result.getString("author"),result.getString("content"),result.getString("url")));
                }
            } catch (JSONException jsonE) {
                Log.e("Movie::updateReviews", "Error during parsing", jsonE);
            }
        }
        Log.d("reviewsJSON:", reviewsJson.toString());
        Log.d("reviews:", this.reviews.toString());
    }

    public void updateTrailers(Context ctxt, JSONObject trailerJson) {
        if (trailerJson.has("results")) {
            DetailActivityFragment.mTrailerAdapter.clear();
            try {
                JSONArray results = trailerJson.getJSONArray("results");
                for(int i=0; i<results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    if(result.getString("type").equals("Trailer")) {
                        if(result.getString("site").equalsIgnoreCase("youtube")) {
                            String url = "https://www.youtube.com/watch?v="+result.getString("key");
                            DetailActivityFragment.mTrailerAdapter.add(new Trailer(url, result.getString("name")));
                        }
                    }
                }
                } catch (JSONException jsonE) {
                Log.e("Movie::updateTrailers", "Error during parsing", jsonE);
            }
        }
        Log.d("trailersJSON:", trailerJson.toString());
        Log.d("trailers:", this.trailers.toString());
    }

    public void backgroundGet(Context ctx) {
        NetworkUtil.getTrailer(ctx, this);
        NetworkUtil.getReviews(ctx, this);
    }
}
