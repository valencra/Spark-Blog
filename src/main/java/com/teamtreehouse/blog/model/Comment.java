package com.teamtreehouse.blog.model;

import java.util.Date;

public class Comment {
    private String author;
    private String body;
    private Date date;

    public Comment(String author, String body) {
        this.author = author;
        this.body = body;
        date = new Date();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (author != null ? !author.equals(comment.author) : comment.author != null) return false;
        if (body != null ? !body.equals(comment.body) : comment.body != null) return false;
        return date != null ? date.equals(comment.date) : comment.date == null;
    }

    @Override
    public int hashCode() {
        int result = author != null ? author.hashCode() : 0;
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
