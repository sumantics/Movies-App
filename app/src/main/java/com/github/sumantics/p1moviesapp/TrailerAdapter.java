package com.github.sumantics.p1moviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.List;
import java.util.zip.Inflater;

public class TrailerAdapter extends ArrayAdapter<Trailer> {
    public TrailerAdapter(Context context, List<Trailer> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view==null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_trailer, parent, false);
        }
        final Trailer trailer = getItem(position);
        TextView trailerView = (TextView) view.findViewById(R.id.trailer_name);
        trailerView.setText(trailer.trailerName);
        trailerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getTrailerURL()) );
                view.getContext().startActivity(videoIntent);
            }
        });
        return view;
    }
}