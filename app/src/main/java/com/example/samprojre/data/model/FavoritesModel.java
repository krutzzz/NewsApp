package com.example.samprojre.data.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.samprojre.base_adapter.ListAdapterItem;
import com.example.samprojre.utils.diffutil.DiffUtilEquality;

@Entity(tableName = "favorites_tbl")
public class FavoritesModel implements ListAdapterItem, DiffUtilEquality {

    @PrimaryKey(autoGenerate = true)
    private int idR;

    private String title;

    private String sourceName;

    private String author;

    private String description;

    private String url;

    private String urlToImage;

    private String publishedAt;


    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }


    public int getIdR() {
        return idR;
    }

    public void setIdR(int id) {
        this.idR = id;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public boolean realEquals(Object toCompare) {
        if (this == toCompare) return true;
        if (toCompare == null || getClass() != toCompare.getClass()) return false;
        FavoritesModel favorite = (FavoritesModel) toCompare;
        return idR == favorite.idR && url.equals(favorite.getUrl());
    }
}
