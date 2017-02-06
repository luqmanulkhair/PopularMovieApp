package de.udacity.luqman.popmoviestag1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.mtitle) TextView title;
    @BindView(R.id.mposter) ImageView poster;
    @BindView(R.id.mrelease) TextView releaseDate;
    @BindView(R.id.mdesc) TextView description;
    @BindView(R.id.mrating) TextView rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            Movie movie = bundle.getParcelable("movie");

            title.setText(movie.getOriginalTitle());
            releaseDate.setText(movie.getReleaseDate());
            description.setText(movie.getPlotSynopsis());
            rating.setText(movie.getUserRatings());
            Picasso.with(this).load(movie.getPosterPath()).into(poster);
        }



    }
}
