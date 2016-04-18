package com.nordlogic.imgursmostviral.data.repositories;

import android.support.annotation.NonNull;

import com.nordlogic.imgursmostviral.data.models.Comment;
import com.nordlogic.imgursmostviral.data.models.Post;

import java.util.List;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public interface PostsRepository {

    interface GetPostsCallback {

        void onPostsLoaded(List<Post> posts);
    }

    interface GetPostCallback {

        void onPostLoaded(Post post);
    }

    interface GetCommentsCallback {
        void onCommentsLoaded(List<Comment> comments);
    }

    void refreshData();

    void getPosts(@NonNull GetPostsCallback callback);

    void getPost(@NonNull String postId, @NonNull GetPostCallback callback);

    void getComments(@NonNull String postId, @NonNull GetCommentsCallback callback);
}
