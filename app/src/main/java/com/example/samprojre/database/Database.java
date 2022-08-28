package com.example.samprojre.database;


import androidx.room.RoomDatabase;

import com.example.samprojre.data.DownloadedDao;
import com.example.samprojre.data.FavoriteNewsDao;
import com.example.samprojre.data.model.DownloadsModel;
import com.example.samprojre.data.model.FavoritesModel;

@androidx.room.Database(entities = {FavoritesModel.class, DownloadsModel.class}, version = 6,exportSchema = false)
public abstract class Database extends RoomDatabase {

    public abstract FavoriteNewsDao FavoriteNewsDao();

    public abstract DownloadedDao DownloadedDao();


}
