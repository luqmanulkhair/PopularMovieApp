package de.udacity.luqman.popmoviestag1;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by luqman on 17.04.2017.
 */

public class MovieContentProvider  extends ContentProvider {

        // The URI Matcher used by this content provider.
        private static final UriMatcher sUriMatcher = buildUriMatcher();
        private MovieHelper movieHelper;

        static final int MOVIES = 100;
        static final int MOVIES_WITH_ID = 101;


        //movie.movieid = ?
        private static final String sMovieIdSelection =
                FavMovieContract.FavMovieEntry.TABLE_NAME +
                        "." + FavMovieContract.FavMovieEntry.COLUMN_MOVIE_ID + " = ?";

        @Override
        public boolean onCreate() {
            movieHelper = new MovieHelper(getContext());
            return true;
        }

        @Override
        public String getType(Uri uri) {
            // Use the Uri Matcher to determine what kind of URI this is.
            final int match = sUriMatcher.match(uri);

            switch (match) {
                case MOVIES_WITH_ID:
                    return FavMovieContract.FavMovieEntry.CONTENT_ITEM_TYPE;
                case MOVIES:
                    return FavMovieContract.FavMovieEntry.CONTENT_TYPE;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        @Override
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                            String sortOrder) {
            // Here's the switch statement that, given a URI, will determine what kind of request it is,
            // and query the database accordingly.
            Cursor retCursor;

            Log.e("uri",uri.toString());
            final int match = sUriMatcher.match(uri);

            switch (match) {
                // "movie/*"
                case MOVIES_WITH_ID:
                {
                    retCursor = getMoviesById(uri, projection, sortOrder);
                    break;
                }
                // "movie"
                case MOVIES: {
                    retCursor = getMovies(uri, projection, selection, selectionArgs, sortOrder);
                    break;
                }

                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);
            return retCursor;
        }

        private Cursor getMoviesById(Uri uri, String[] projection, String sortOrder) {
            return movieHelper.getReadableDatabase().query(
                    FavMovieContract.FavMovieEntry.TABLE_NAME,
                    projection,
                    sMovieIdSelection,
                    new String[]{FavMovieContract.FavMovieEntry.getIdFromUri(uri)},
                    null,
                    null,
                    sortOrder
            );
        }

        private Cursor getMovies(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
            return movieHelper.getReadableDatabase().query(
                    FavMovieContract.FavMovieEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
        }

        @Override
        public Uri insert(Uri uri, ContentValues contentValues) {

            Uri returnUri;

            int match = sUriMatcher.match(uri);

            switch (match) {
                // "movie"
                case MOVIES: {
                    long _id = movieHelper.getWritableDatabase().insert(FavMovieContract.FavMovieEntry.TABLE_NAME, null, contentValues);
                    if(_id > 0){
                        returnUri = FavMovieContract.FavMovieEntry.buildMovieUri(_id);
                    }
                    else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                    break;
                }

                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            getContext().getContentResolver().notifyChange(uri,null);

            return returnUri;
        }

        @Override
        public int delete(Uri uri, String selection, String[] selectionArgs) {

            int deletedRows = 0;

            if(selection == null) selection = "1";

            int match = sUriMatcher.match(uri);

            switch (match) {
                // "movie"
                case MOVIES: {
                    deletedRows = movieHelper.getWritableDatabase().delete(FavMovieContract.FavMovieEntry.TABLE_NAME, selection, selectionArgs);

                    break;
                }

                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            if(deletedRows > 0 ){
                getContext().getContentResolver().notifyChange(uri,null);
            }

            return deletedRows;
        }

        @Override
        public int update(
                Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            SQLiteDatabase db = movieHelper.getWritableDatabase();

            int match = buildUriMatcher().match(uri);

            int rowsUpdated = 0;

            if (selection == null) selection = "1";

            switch (match) {

                case MOVIES: {
                    rowsUpdated = db.update(FavMovieContract.FavMovieEntry.TABLE_NAME, values, selection, selectionArgs);
                    break;
                }
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            if (rowsUpdated > 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }

            return rowsUpdated;
        }

        // TODO need to implement a better bulk insert


        /*
         */
        static UriMatcher buildUriMatcher() {
            // 1) The code passed into the constructor represents the code to return for the root
            // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.

            final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);


            // 2) Use the addURI function to match each of the types.  Use the constants from
            // MovieContract to help define the types to the UriMatcher.
            sURIMatcher.addURI(FavMovieContract.CONTENT_AUTHORITY, FavMovieContract.PATH_MOVIE, MOVIES);
            sURIMatcher.addURI(FavMovieContract.CONTENT_AUTHORITY, FavMovieContract.PATH_MOVIE + "/*", MOVIES_WITH_ID);

            // 3) Return the new matcher!
            return sURIMatcher;
        }
    }
