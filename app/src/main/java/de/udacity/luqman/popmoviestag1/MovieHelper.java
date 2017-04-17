package de.udacity.luqman.popmoviestag1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by luqman on 17.04.2017.
 */

public class MovieHelper extends SQLiteOpenHelper {

        public static int DATABASE_VERSION = 1;

        public static String DATABASE_NAME = "movies.db";

        public MovieHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + FavMovieContract.FavMovieEntry.TABLE_NAME + " (" +

                    FavMovieContract.FavMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                    FavMovieContract.FavMovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                    FavMovieContract.FavMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                    FavMovieContract.FavMovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                    FavMovieContract.FavMovieEntry.COLUMN_YEAR + " INTEGER NOT NULL, " +
                    FavMovieContract.FavMovieEntry.COLUMN_RATING + " REAL NOT NULL, " +
                    FavMovieContract.FavMovieEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +

                    " UNIQUE (" + FavMovieContract.FavMovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

            sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavMovieContract.FavMovieEntry.TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }
