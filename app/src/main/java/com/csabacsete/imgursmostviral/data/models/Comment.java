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
    private int childrenSize;
    private List<Comment> children;

    public Comment(int id, String comment, String author, int points, long datetime, int childrenSize, List<Comment> children) {
        this.id = id;
        this.comment = comment;
        this.author = author;
        this.points = points;
        this.datetime = datetime;
        this.childrenSize = childrenSize;
        this.children = children;
    }

    public static Comment cloneWithoutChildren(Comment c) {
        return new Comment(
                c.getId(),
                c.getComment(),
                c.getAuthor(),
                c.getPoints(),
                c.getDatetime(),
                c.getChildren().size(),
                null
        );
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

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public int getChildrenSize() {
        return childrenSize;
    }

    public void setChildrenSize(int childrenSize) {
        this.childrenSize = childrenSize;
    }

    public List<Comment> getChildren() {
        return children;
    }

    public void setChildren(List<Comment> children) {
        this.children = children;
    }
}
