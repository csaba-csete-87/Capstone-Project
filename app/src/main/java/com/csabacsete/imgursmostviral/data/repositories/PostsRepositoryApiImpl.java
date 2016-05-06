package com.csabacsete.imgursmostviral.data.repositories;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.csabacsete.imgursmostviral.data.api.PostsServiceApi;
import com.csabacsete.imgursmostviral.data.models.Post;
import com.csabacsete.imgursmostviral.data.responses.CommentsResponse;
import com.csabacsete.imgursmostviral.data.responses.PostResponse;
import com.csabacsete.imgursmostviral.data.responses.PostsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public class PostsRepositoryApiImpl implements PostsRepository {

    private final PostsServiceApi mPostsServiceApi;

    /**
     * This method has reduced visibility for testing and is only visible to tests in the same
     * package.
     */
    @VisibleForTesting
    List<Post> mCachedPosts;

    public PostsRepositoryApiImpl(@NonNull PostsServiceApi postsServiceApi) {
        mPostsServiceApi = checkNotNull(postsServiceApi);
    }

    @Override
    public void getPosts(@NonNull final GetPostsCallback callback) {
        checkNotNull(callback);
        // Load from API only if needed.
        if (mCachedPosts == null) {
            Call<PostsResponse> postsCall = mPostsServiceApi.getViralPosts();
            postsCall.enqueue(new Callback<PostsResponse>() {
                @Override
                public void onResponse(Call<PostsResponse> call, Response<PostsResponse> response) {
                    if (response.isSuccess()) {
                        callback.onPostsLoaded(response.body().getPosts());
                    } else {
                        callback.onPostsLoaded(new ArrayList<Post>());
                    }
                }

                @Override
                public void onFailure(Call<PostsResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            callback.onPostsLoaded(mCachedPosts);
        }
    }

    @Override
    public void refreshData() {
        mCachedPosts = null;
    }

    @Override
    public void getPost(@NonNull final String id, @NonNull final GetPostCallback getPostCallback) {
        final String postId = checkNotNull(id);
        final GetPostCallback callback = checkNotNull(getPostCallback);

        Call<PostResponse> getPostCall = mPostsServiceApi.getPostById(postId);
        getPostCall.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccess()) {
                    callback.onPostLoaded(response.body().getPost());
                } else {
                    callback.onPostLoaded(null);
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void getComments(@NonNull final String id, final String sort, final GetCommentsCallback getCommentsCallback) {
        final String postId = checkNotNull(id);
        final GetCommentsCallback callback = checkNotNull(getCommentsCallback);

        Call<CommentsResponse> getCommentsCall = mPostsServiceApi.getCommentsByPostId(postId, sort);
        getCommentsCall.enqueue(new Callback<CommentsResponse>() {
            @Override
            public void onResponse(Call<CommentsResponse> call, Response<CommentsResponse> response) {
                if (response.isSuccess()) {
                    callback.onCommentsLoaded(response.body().getComments());
                } else {
                    callback.onCommentsLoaded(null);
                }
            }

            @Override
            public void onFailure(Call<CommentsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
