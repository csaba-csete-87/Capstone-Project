package com.csabacsete.imgursmostviral.data.repositories;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.csabacsete.imgursmostviral.data.db.ImgurContract;
import com.csabacsete.imgursmostviral.data.models.Post;

import java.util.ArrayList;


/**
 * Created by ccsete on 5/2/16.
 */
public class PostsRepositoryLocalImpl implements PostsRepository, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int POSTS_LOADER = 0;

    private static final String[] POST_COLUMNS = {
            ImgurContract.PostEntry.COLUMN_POST_ID,
            ImgurContract.PostEntry.COLUMN_TITLE,
            ImgurContract.PostEntry.COLUMN_DESCRIPTION,
            ImgurContract.PostEntry.COLUMN_LINK,
            ImgurContract.PostEntry.COLUMN_TYPE,
            ImgurContract.PostEntry.COLUMN_GIFV,
            ImgurContract.PostEntry.COLUMN_DATETIME,
            ImgurContract.PostEntry.COLUMN_COVER,
            ImgurContract.PostEntry.COLUMN_IS_ALBUM,
    };
    private static final int COL_INDEX_POST_ID = 0;
    private static final int COL_INDEX_TITLE = 1;
    private static final int COL_INDEX_DESCRIPTION = 2;
    private static final int COL_INDEX_LINK = 3;
    private static final int COL_INDEX_TYPE = 4;
    private static final int COL_INDEX_GIFV = 5;
    private static final int COL_INDEX_DATETIME = 6;
    private static final int COL_INDEX_COVER = 7;
    private static final int COL_INDEX_IS_ALBUM = 8;

    private final Fragment fragment;
    private GetPostsCallback getPostsCallback;

    public PostsRepositoryLocalImpl(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void refreshData() {
    }

    @Override
    public void getPosts(@NonNull GetPostsCallback callback) {
        getPostsCallback = callback;
        fragment.getLoaderManager().initLoader(POSTS_LOADER, null, this);
    }

    @Override
    public void getPost(@NonNull String postId, @NonNull GetPostCallback callback) {
        callback.onPostLoaded(null);
    }

    @Override
    public void getComments(@NonNull String postId, String sort, @NonNull GetCommentsCallback callback) {
        callback.onCommentsLoaded(null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.
        String sortOrder = ImgurContract.PostEntry._ID + " ASC";

        Uri allPostsUri = ImgurContract.PostEntry.buildAllPosts();

        return new CursorLoader(fragment.getActivity(),
                allPostsUri,
                POST_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        getPostsCallback.onPostsLoaded(getPostListFromCursor(cursor));
    }

    @NonNull
    private ArrayList<Post> getPostListFromCursor(Cursor cursor) {
        ArrayList<Post> posts = new ArrayList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            Post p = new Post(
                    cursor.getString(COL_INDEX_POST_ID),
                    cursor.getString(COL_INDEX_TITLE),
                    cursor.getString(COL_INDEX_DESCRIPTION),
                    cursor.getString(COL_INDEX_LINK),
                    cursor.getString(COL_INDEX_TYPE),
                    cursor.getString(COL_INDEX_GIFV),
                    cursor.getLong(COL_INDEX_DATETIME),
                    cursor.getString(COL_INDEX_COVER),
                    getBooleanFromInt(cursor.getInt(COL_INDEX_IS_ALBUM))
            );

            posts.add(p);
        }
        return posts;
    }

    private boolean getBooleanFromInt(int isAlbum) {
        return isAlbum == 1;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
