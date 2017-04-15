package de.udacity.luqman.popmoviestag1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by luqman on 15.04.2017.
 */

public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(Activity context, List<Review> reviews) {
        super(context, 0, reviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Review review = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.review_list_item, parent, false);
        }

        TextView authorTextView = (TextView) convertView.findViewById(R.id.author);
        authorTextView.setText(review.getAuthor());

        TextView reviewTextView = (TextView) convertView.findViewById(R.id.review);
        reviewTextView.setText(review.getContent());

        return convertView;
    }

}
