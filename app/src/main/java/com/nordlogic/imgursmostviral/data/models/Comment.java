package com.nordlogic.imgursmostviral.data.models;

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

    public int getId() {
        return id;
    }

    public String getComment() {
        return comment;
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

    public void setChildren(List<Comment> children) {
        this.children = children;
    }

    public int getChildrenSize() {
        return childrenSize;
    }

    public void setChildrenSize(int childrenSize) {
        this.childrenSize = childrenSize;
    }
}
