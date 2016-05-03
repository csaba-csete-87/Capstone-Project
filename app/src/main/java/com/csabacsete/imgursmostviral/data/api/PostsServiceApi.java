package com.csabacsete.imgursmostviral.data.api;

import com.csabacsete.imgursmostviral.BuildConfig;
import com.csabacsete.imgursmostviral.data.responses.CommentsResponse;
import com.csabacsete.imgursmostviral.data.responses.PostResponse;
import com.csabacsete.imgursmostviral.data.responses.PostsResponse;

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

    @Headers({
            "Authorization: Client-ID " + BuildConfig.IMGUR_CLIENT_ID,
            "Cache-Control: max-age=60"
    })
    @GET("/3/gallery/{id}/comments/{sort}")
    Call<CommentsResponse> getCommentsByPostId(
            @Path("id") String postId,
            @Path("sort") String sort
    );
}
