package de.udacity.luqman.popmoviestag1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by luqman on 02.02.2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private Context context;
    private ArrayList<Movie> movieList;

    public MoviesAdapter(Context context,ArrayList<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_grid_itm,parent,false);
        MoviesAdapterViewHolder viewHolder = new MoviesAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        Picasso.with(context).load(movieList.get(position).getPosterPath()).into(holder.gridItemMoviePoster);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder
            implements  View.OnClickListener {

        ImageView gridItemMoviePoster;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            gridItemMoviePoster = (ImageView) itemView.findViewById(R.id.movie_poster_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            Log.i("Position Clicked", String.valueOf(position));

            Movie movie = movieList.get(position);
            Intent intent = new Intent();

            Bundle bundle = new Bundle();
            bundle.putParcelable("movie", movie);
            intent.putExtras(bundle);
            intent.setClass(context, MovieDetailActivity.class);
            context.startActivity(intent);


        }
    }


}
