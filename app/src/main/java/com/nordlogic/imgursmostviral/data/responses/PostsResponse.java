package com.nordlogic.imgursmostviral.data.responses;

import com.nordlogic.imgursmostviral.data.models.Post;

import java.util.List;

/**
 * Created by csaba.csete on 2016-02-25.
 */
public class PostsResponse {
    private List<Post> data;

    public List<Post> getPosts() {
        return data;
    }

}
