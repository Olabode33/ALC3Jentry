package com.olabode33.android.alc3jentry.Model;

import com.google.firebase.database.Exclude;

import java.util.Date;

/**
 * Created by obello004 on 6/28/2018.
 */

public class JournalEntry {
    @Exclude
    private String key;
    private String entry;
    private String username;
    private String createdAt;
    private String updateAt;

    public JournalEntry() {

    }

    public JournalEntry(String entry, String name, String createdAt, String updateAt) {
        this.entry = entry;
        this.username = name;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String uid) {
        this.key = uid;
    }
}
