package com.teamtreehouse.blog.model;

import com.github.slugify.Slugify;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Entry {
    private String title;
    private String body;
    private Date date;
    private List<Comment> comments;
    private List<String> tags;
    private String slug;

    public Entry(String title, String body) {
        this.title = title;
        this.body = body;
        date = new Date();
        comments = new ArrayList<>();
        tags = new ArrayList<>();
        try {
            Slugify slugify = new Slugify();
            slug = slugify.slugify(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDateAttribute() {
        return new SimpleDateFormat("yyyy-MM-dd H:MM").format(date);
    }

    public String getDateText() {
        return new SimpleDateFormat("MMMMM dd, yyyy 'at' h:mm").format(date);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getSlug() {
        return slug;
    }

    public boolean addComment(Comment comment) {
        return comments.add(comment);
    }

    public boolean addTag(String tag) {
        return tags.add(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entry entry = (Entry) o;

        if (title != null ? !title.equals(entry.title) : entry.title != null) return false;
        if (body != null ? !body.equals(entry.body) : entry.body != null) return false;
        if (date != null ? !date.equals(entry.date) : entry.date != null) return false;
        if (comments != null ? !comments.equals(entry.comments) : entry.comments != null) return false;
        if (tags != null ? !tags.equals(entry.tags) : entry.tags != null) return false;
        return slug != null ? slug.equals(entry.slug) : entry.slug == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (slug != null ? slug.hashCode() : 0);
        return result;
    }
}
