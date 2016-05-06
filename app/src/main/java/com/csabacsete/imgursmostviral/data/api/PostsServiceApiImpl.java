package com.csabacsete.imgursmostviral.data.api;

import com.csabacsete.imgursmostviral.BuildConfig;
import com.csabacsete.imgursmostviral.data.responses.CommentsResponse;
import com.csabacsete.imgursmostviral.data.responses.PostResponse;
import com.csabacsete.imgursmostviral.data.responses.PostsResponse;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Path;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public class PostsServiceApiImpl implements PostsServiceApi {

    private static PostsServiceApi postsServiceApi = null;

    public PostsServiceApiImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.IMGUR_API_BASE_URL)
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
    public Call<CommentsResponse> getCommentsByPostId(@Path("id") String postId, @Path("sort") String sort) {
        return postsServiceApi.getCommentsByPostId(postId, sort);
    }

}
