package com.example.samprojre.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.samprojre.base_adapter.ListAdapterItem;
import com.example.samprojre.utils.diffutil.DiffUtilEquality;


@Entity(tableName = "downloaded_tbl")
public class DownloadsModel implements ListAdapterItem, DiffUtilEquality {

    @PrimaryKey(autoGenerate = true)
    private int idR;

    private String path;

    private String imgFilePath;

    private String publishedAt;

    private String title;

    private String source;

    private String author;

    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImgFilePath() {
        return imgFilePath;
    }

    public void setImgFilePath(String imgFilePath) {
        this.imgFilePath = imgFilePath;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }


    @Override
    public boolean realEquals(Object toCompare) {
        if (this == toCompare) return true;
        if (toCompare == null || getClass() != toCompare.getClass()) return false;
        DownloadsModel downloadsModel = (DownloadsModel) toCompare;
        return idR == downloadsModel.idR && path.equals(downloadsModel.getPath());
    }

    @Override
    public long getId() {
        return 0;
    }

    public void setIdR(int idR) {
        this.idR = idR;
    }

    public int getIdR() {
        return idR;
    }
}
