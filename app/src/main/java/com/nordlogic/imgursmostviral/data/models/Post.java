package com.nordlogic.imgursmostviral.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public class Post {

    private static final String COVER_TEMPLATE = "http://i.imgur.com/%sm.jpg";

    private String id;
    private String title;
    private String cover;
    @SerializedName("is_album")
    @Expose
    private boolean isAlbum;

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

    private boolean isAlbum() {
        return isAlbum;
    }

}
