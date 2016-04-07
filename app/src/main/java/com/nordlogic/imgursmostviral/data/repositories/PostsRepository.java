package com.nordlogic.imgursmostviral.data.repositories;

import android.support.annotation.NonNull;

import com.nordlogic.imgursmostviral.data.models.Post;

import java.util.List;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public interface PostsRepository {

    interface LoadPostsCallback {

        void onPostsLoaded(List<Post> posts);
    }

    interface GetPostCallback {

        void onPostLoaded(Post post);
    }

    void getPosts(@NonNull LoadPostsCallback callback);

    void refreshData();

    void getPost(@NonNull String postId, @NonNull GetPostCallback callback);
}
