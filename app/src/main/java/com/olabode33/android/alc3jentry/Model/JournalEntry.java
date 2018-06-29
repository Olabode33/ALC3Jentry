package com.olabode33.android.alc3jentry.Model;

import java.util.Date;

/**
 * Created by obello004 on 6/28/2018.
 */

public class JournalEntry {
    private String entry;
    private String username;
    private Date createdAt;
    private Date updateAt;

    public JournalEntry(){

    }

    public JournalEntry(String entry, String name, Date createdAt, Date updateAt){
        this.entry = entry;
        this.username = name;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public String getEntry(){
        return entry;
    }

    public void setEntry(String entry){
        this.entry = entry;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String name){
        this.username = name;
    }

    public Date getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(Date createdAt){
        this.createdAt = createdAt;
    }

    public Date getUpdateAt(){
        return updateAt;
    }

    public void setUpdateAt(Date updateAt){
        this.updateAt = updateAt;
    }
}
