package com.csabacsete.imgursmostviral.data.responses;

import com.csabacsete.imgursmostviral.data.models.Comment;

import java.util.List;

/**
 * Created by ccsete on 4/18/16.
 */
public class CommentsResponse {

    private List<Comment> data;

    public List<Comment> getComments() {
        return data;
    }
}
