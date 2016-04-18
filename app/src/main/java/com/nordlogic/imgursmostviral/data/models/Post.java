package com.nordlogic.imgursmostviral.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public class Post {

    public static final int PAGE_COUNT = 10;

    private static final String COVER_TEMPLATE = "http://i.imgur.com/%sm.jpg";

    private String id;
    private String title;
    private String cover;
    @SerializedName("images_count")
    @Expose
    private int imagesCount;
    @SerializedName("comment_count")
    @Expose
    private int commentCount;
    @SerializedName("is_album")
    @Expose
    private boolean isAlbum;

    private List<Comment> comments;

    public Post(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return getUrl(isAlbum() ? getCover() : getId());
    }

    private String getUrl(String id) {
        return String.format(COVER_TEMPLATE, id);
    }

    private String getCover() {
        return cover;
    }

    public boolean isAlbum() {
        return isAlbum;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getImagesCount() {
        return imagesCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
