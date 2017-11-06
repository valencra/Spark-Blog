package com.teamtreehouse.blog.dao;

import com.teamtreehouse.blog.exception.NotFoundException;
import com.teamtreehouse.blog.model.BlogEntry;

import java.util.ArrayList;
import java.util.List;

public class BlogDAOImpl implements BlogDAO {
    private List<BlogEntry> entries;

    public BlogDAOImpl() {
        entries = new ArrayList<>();
    }

    @Override
    public boolean addEntry(BlogEntry blogEntry) {
        return entries.add(blogEntry);
    }

    @Override
    public List<BlogEntry> findAllEntries() {
        return entries;
    }

    @Override
    public BlogEntry findEntryBySlug(String slug) {
        return entries.stream()
                .filter(entry -> entry.getSlug().equals(slug))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
