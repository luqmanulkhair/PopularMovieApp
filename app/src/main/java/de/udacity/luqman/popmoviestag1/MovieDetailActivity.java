package de.udacity.luqman.popmoviestag1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView title;
    private ImageView poster;
    private TextView releaseDate;
    private TextView description;
    private TextView rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            Movie movie = bundle.getParcelable("movie");

            title = (TextView) findViewById(R.id.mtitle);
            title.setText(movie.getOriginalTitle());

            releaseDate = (TextView) findViewById(R.id.mrelease);
            releaseDate.setText(movie.getReleaseDate());

            description = (TextView) findViewById(R.id.mdesc);
            description.setText(movie.getPlotSynopsis());

            rating = (TextView) findViewById(R.id.mrating);
            rating.setText(movie.getUserRatings());

            poster = (ImageView) findViewById(R.id.mposter);
            Picasso.with(this).load(movie.getPosterPath()).into(poster);
        }



    }
}
