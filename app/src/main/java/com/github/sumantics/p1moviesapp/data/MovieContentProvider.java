package com.github.sumantics.p1moviesapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

public class MovieContentProvider extends ContentProvider{

    static final int MOVIE = 100;
    static final int MOVIE_WITH_ID = 101;

    private MovieDbHelper movieDbHelper;
    private UriMatcher sUriMatcher = buildUriMatcher();

    private static final SQLiteQueryBuilder sQueryBuilder = new SQLiteQueryBuilder();;

    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        Uri returnUri;
        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
        if ( _id > 0 )
            returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
        else
            throw new android.database.SQLException("Failed to insert row into " + uri);//we can only add MOVIE_ITEM
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;
        String groupBy=null, having = null;
        switch (sUriMatcher.match(uri)){
            case MOVIE:
                retCursor = sQueryBuilder.query(movieDbHelper.getReadableDatabase(), projection, selection, selectionArgs, groupBy, having, sortOrder);
            case MOVIE_WITH_ID: {
                String custSelection = MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?";
                retCursor = sQueryBuilder.query(movieDbHelper.getReadableDatabase(), projection, custSelection, selectionArgs, groupBy, having, sortOrder);
            }
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection == null ? "1" : selection, selectionArgs);
        if(rowsDeleted!=0)  getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case MOVIE:         return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID: return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVMOVIE, MOVIE);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVMOVIE + "/*", MOVIE_WITH_ID); //# for numbers
        return matcher;
    }
    //add entries in manifest.xml
}
