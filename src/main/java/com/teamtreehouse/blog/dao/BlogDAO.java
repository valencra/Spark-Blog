package com.teamtreehouse.blog.dao;

import com.teamtreehouse.blog.model.Entry;

import java.util.List;

public interface BlogDAO {
    boolean addEntry(Entry entry);

    boolean deleteEntry(Entry entry);

    List<Entry> findAllEntries();

    Entry findEntryBySlug(String slug);
}
