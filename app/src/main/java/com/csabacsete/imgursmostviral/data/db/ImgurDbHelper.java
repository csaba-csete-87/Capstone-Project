package com.csabacsete.imgursmostviral.data.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.csabacsete.imgursmostviral.data.db.ImgurContract.PostEntry;
import com.csabacsete.imgursmostviral.data.models.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a local database for weather data.
 */
public class ImgurDbHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "imgur.db";
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public ImgurDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + PostEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                PostEntry.COLUMN_POST_ID + " TEXT NOT NULL, " +
                PostEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                PostEntry.COLUMN_IMAGES_COUNT + " INTEGER, " +
                PostEntry.COLUMN_POINTS + " INTEGER NOT NULL," +
                PostEntry.COLUMN_COVER + " TEXT, " +
                PostEntry.COLUMN_DATETIME + " TEXT NOT NULL, " +
                PostEntry.COLUMN_DESCRIPTION + " TEXT, " +
                PostEntry.COLUMN_LINK + " TEXT NOT NULL, " +
                PostEntry.COLUMN_TYPE + " TEXT, " +
                PostEntry.COLUMN_GIFV + " TEXT, " +
                PostEntry.COLUMN_COMMENT_COUNT + " INTEGER NOT NULL, " +
                PostEntry.COLUMN_IS_ALBUM + " TEXT NOT NULL, " +
                PostEntry.COLUMN_ACCOUNT_URL + " TEXT, " +

                " UNIQUE (" + PostEntry.COLUMN_POST_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PostEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public List<Post> getAllPosts() {
        SQLiteDatabase database = getWritableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM " + PostEntry.TABLE_NAME, null);
        List<Post> posts = getPostListFromCursor(c);
        c.close();
        return posts;
    }

    @NonNull
    public static ArrayList<Post> getPostListFromCursor(Cursor cursor) {
        ArrayList<Post> posts = new ArrayList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            Post p = new Post(
                    cursor.getString(cursor.getColumnIndex(PostEntry.COLUMN_POST_ID)),
                    cursor.getString(cursor.getColumnIndex(PostEntry.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(PostEntry.COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(PostEntry.COLUMN_LINK)),
                    cursor.getString(cursor.getColumnIndex(PostEntry.COLUMN_TYPE)),
                    cursor.getString(cursor.getColumnIndex(PostEntry.COLUMN_GIFV)),
                    cursor.getLong(cursor.getColumnIndex(PostEntry.COLUMN_DATETIME)),
                    cursor.getString(cursor.getColumnIndex(PostEntry.COLUMN_COVER)),
                    getBooleanFromInt(cursor.getInt(cursor.getColumnIndex(PostEntry.COLUMN_IS_ALBUM)))
            );

            posts.add(p);
        }
        return posts;
    }

    private static boolean getBooleanFromInt(int isAlbum) {
        return isAlbum == 1;
    }
}
