package com.nordlogic.imgursmostviral.data.api;

import com.nordlogic.imgursmostviral.data.responses.CommentsResponse;
import com.nordlogic.imgursmostviral.data.responses.PostResponse;
import com.nordlogic.imgursmostviral.data.responses.PostsResponse;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Path;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public class PostsServiceApiImpl implements PostsServiceApi {

    private static PostsServiceApi postsServiceApi = null;

    // TODO: 2016-02-24 add string to gradle

    public PostsServiceApiImpl() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.imgur.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        postsServiceApi = retrofit.create(PostsServiceApi.class);
    }

    @Override
    public Call<PostsResponse> getViralPosts() {
        return postsServiceApi.getViralPosts();
    }

    @Override
    public Call<PostResponse> getPostById(@Path("id") String postId) {
        return postsServiceApi.getPostById(postId);
    }

    @Override
    public Call<CommentsResponse> getCommentsByPostId(@Path("id") String postId) {
        return postsServiceApi.getCommentsByPostId(postId);
    }
}
