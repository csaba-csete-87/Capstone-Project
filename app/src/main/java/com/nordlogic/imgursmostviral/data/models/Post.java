package com.nordlogic.imgursmostviral.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public class Post extends Image {

    public static final int PAGE_COUNT = 10;

    private static final String COVER_TEMPLATE = "http://i.imgur.com/%sm.jpg";
    public static final String TYPE_GIF = "image/gif";

    private String cover;
    private int points;
    @SerializedName("images_count")
    @Expose
    private int imagesCount;
    @SerializedName("comment_count")
    @Expose
    private int commentCount;
    @SerializedName("is_album")
    @Expose
    private boolean isAlbum;
    @SerializedName("account_url")
    @Expose
    private String accountUrl;

    private List<Comment> comments;
    private List<Image> images;

    public Post(String title, String description, String link, String type, long datetime) {
        super(title, description, link, type, datetime);
    }

    public String getThumbnail() {
        return getUrl(isAlbum() ? getCover() : getId());
    }

    private String getUrl(String id) {
        return String.format(COVER_TEMPLATE, id);
    }

    public String getCover() {
        return cover;
    }

    public int getPoints() {
        return points;
    }

    public int getImagesCount() {
        return imagesCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public boolean isAlbum() {
        return isAlbum;
    }

    public String getAccountUrl() {
        return accountUrl;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<Image> getImages() {
        return images;
    }
}
