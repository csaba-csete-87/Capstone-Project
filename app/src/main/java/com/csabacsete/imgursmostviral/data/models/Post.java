package com.csabacsete.imgursmostviral.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public class Post extends Image {

    private static final String COVER_TEMPLATE = "http://i.imgur.com/%sm.jpg";
    public static final String TYPE_GIF = "image/gif";

    public static final String SORT_TYPE_BEST = "Best";
    public static final String SORT_TYPE_TOP = "Top";
    public static final String SORT_TYPE_NEWEST = "Newest";
    public static final String[] SORT_TYPES = {SORT_TYPE_BEST, SORT_TYPE_TOP, SORT_TYPE_NEWEST};

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

    public Post(String id, String title, String description, String link, String type, String gifv, long datetime, String cover, boolean isAlbum) {
        super(id, title, description, link, type, gifv, datetime);
        this.cover = cover;
        this.isAlbum = isAlbum;
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

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setAlbum(boolean album) {
        isAlbum = album;
    }
}
