package com.github.sumantics.p1moviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class DetailActivity extends AppCompatActivity {
    static Movie movie;
    static String LOGTAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movie = (Movie)getIntent().getExtras().getParcelable("movieDetail");
        Log.d(LOGTAG,"onCreate "+movie);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_detail, new DetailActivityFragment()).commit();//The child view is already present (via xml), do I remove?
        }
    }
}
