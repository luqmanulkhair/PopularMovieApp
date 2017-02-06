package de.udacity.luqman.popmoviestag1;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by luqman on 02.02.2017.
 */

public class MovieDBUtils {

    public static final String BASEURL = "http://api.themoviedb.org/3/movie/";
    public static final String PARAM_QUERY = "api_key";


    public static ArrayList<Movie> downloadMovies(String filter) throws IOException, JSONException {

        Uri uri = Uri.parse(BASEURL+filter).buildUpon().
                    appendQueryParameter(PARAM_QUERY,Constants.APIKEY).build();




            URL url = new URL(uri.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            BufferedReader reader;

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            return parseMovieData(buffer.toString());

    }

    private static ArrayList<Movie> parseMovieData(String movieResponse) throws JSONException {

        JSONObject movieJSONResponse = new JSONObject(movieResponse);
        JSONArray moviesJSONList = movieJSONResponse.getJSONArray("results");

        ArrayList<Movie> moviesList = new ArrayList<Movie>();

        for (int i=0;i<moviesJSONList.length();i++){

            JSONObject movieJSONObj = moviesJSONList.getJSONObject(i);

            String title = movieJSONObj.optString("title");
            String releaseDate = movieJSONObj.optString("release_date").split("-")[0];

            String overview = movieJSONObj.optString("overview");
            String rating = movieJSONObj.optString("vote_average").concat("/10");
            String posterPath = movieJSONObj.getString("poster_path");

            Movie movie = new Movie(title,overview,rating,releaseDate,posterPath);
            moviesList.add(movie);
        }

        return moviesList;
    }


}
