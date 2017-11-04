package com.teamtreehouse.blog.dao;

import com.teamtreehouse.blog.model.BlogEntry;

import java.util.List;

public class BlogDAOImpl implements BlogDAO {
    @Override
    public boolean addEntry(BlogEntry blogEntry) {
        return false;
    }

    @Override
    public List<BlogEntry> findAllEntries() {
        return null;
    }

    @Override
    public BlogEntry findEntryBySlug(String slug) {
        return null;
    }
}
