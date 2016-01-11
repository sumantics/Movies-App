package com.github.sumantics.p1moviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback{

    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //commented after dual pane
        //setSupportActionBar(toolbar);
        setContentView(R.layout.activity_main);
        //new code after dual-pane
        if(findViewById(R.id.detail_container)!=null){
            mTwoPane = true;
            if (savedInstanceState == null) {
                Log.d("MainActivity", " : " + findViewById(R.id.fragment_detail)+" : "+ findViewById(R.id.detail_container)+ ":" + savedInstanceState);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_container, new DetailActivityFragment()).commit();
            }
        }else{
            mTwoPane = false;
        }
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Movie movie) {
        Log.d("onItemSelected","mTwoPane:"+mTwoPane+ movie);
        if(!mTwoPane) {
            Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.putExtra("movieDetail", movie);
            startActivity(detailIntent);
        }else{
            Bundle args = new Bundle();
            args.putParcelable("movieDetail", movie);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);
            Log.d("onItemSelected", "args:" + args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment).commit();
        }
    }
}