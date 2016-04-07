package com.nordlogic.imgursmostviral.data.api;

import com.nordlogic.imgursmostviral.BuildConfig;
import com.nordlogic.imgursmostviral.data.responses.PostResponse;
import com.nordlogic.imgursmostviral.data.responses.PostsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public interface PostsServiceApi {

    @Headers({
        "Authorization: Client-ID " + BuildConfig.IMGUR_CLIENT_ID,
        "Cache-Control: max-age=60"
    })
    @GET("/3/gallery/hot/viral/true")
    Call<PostsResponse> getViralPosts();

    @Headers({
        "Authorization: Client-ID " + BuildConfig.IMGUR_CLIENT_ID,
        "Cache-Control: max-age=60"
    })
    @GET("/3/gallery/{id}")
    Call<PostResponse> getPostById(@Path("id") String postId);
}
