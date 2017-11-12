package com.teamtreehouse.blog.dao;

import com.teamtreehouse.blog.exception.NotFoundException;
import com.teamtreehouse.blog.model.Blog;
import com.teamtreehouse.blog.model.Entry;

import java.util.List;

public class BlogDAOImpl implements BlogDAO {
    private Blog blog;

    public BlogDAOImpl() {
        blog = new Blog();
    }

    @Override
    public boolean addEntry(Entry entry) {
        return blog.addEntry(entry);
    }

    @Override
    public List<Entry> findAllEntries() {
        return blog.getEntries();
    }

    @Override
    public Entry findEntryBySlug(String slug) {
        return blog.getEntries().stream()
                .filter(entry -> entry.getSlug().equals(slug))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
