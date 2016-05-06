package com.csabacsete.imgursmostviral.data.models;

import java.util.List;

/**
 * Created by ccsete on 4/18/16.
 */
public class Comment {

    private int id;
    private String comment;
    private String author;
    private int points;
    private long datetime;
    private List<Comment> children;

    public Comment(int id, String comment, String author, int points, long datetime, List<Comment> children) {
        this.id = id;
        this.comment = comment;
        this.author = author;
        this.points = points;
        this.datetime = datetime;
        this.children = children;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public int getPoints() {
        return points;
    }

    public long getDatetime() {
        return datetime;
    }

    public List<Comment> getChildren() {
        return children;
    }
}
