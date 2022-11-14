package com.hcmus.newsreaderapp;

import androidx.annotation.NonNull;

public class SingleItem {
    private final String pubDate;
    private final String title;
    private final String description;
    private String link;

    public SingleItem(String _pubDate, String _title, String _description, String _link) {
        pubDate = _pubDate;
        description = _description;
        title = _title;
        link = _link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}