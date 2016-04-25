package com.nordlogic.imgursmostviral.data.models;

/**
 * Created by ccsete on 4/20/16.
 */
public class Image {

    private String id;
    private String title;
    private String description;
    private String link;
    private String type;
    private String gifv;
    private long datetime;

    public Image(String title, String description, String link, String type, long datetime) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.type = type;
        this.datetime = datetime;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getType() {
        return type;
    }

    public String getGifv() {
        return gifv;
    }

    public long getDatetime() {
        return datetime;
    }
}
