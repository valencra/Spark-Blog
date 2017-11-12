package com.teamtreehouse.blog.model;

import java.util.ArrayList;
import java.util.List;

public class Blog {
    private List<Entry> entries;

    public Blog() {
        entries = new ArrayList<>();
    }

    public boolean addEntry(Entry entry) {
        return entries.add(entry);
    }

    public List<Entry> getEntries() {
        return entries;
    }
}
