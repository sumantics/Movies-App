package com.github.sumantics.p1moviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(Context context, List<Review> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        Review review  = getItem(i);
        if(view==null){
             view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_review, parent, false);
        }
        TextView reviewContent = (TextView) view.findViewById(R.id.review_content);
        reviewContent.setText(review.content);
        TextView reviewAuthor = (TextView) view.findViewById(R.id.review_author);
        reviewAuthor.setText(review.author);

        return view;
    }
}
