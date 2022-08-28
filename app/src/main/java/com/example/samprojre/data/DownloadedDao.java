package com.example.samprojre.data;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.example.samprojre.data.model.DownloadsModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

@Dao
public interface DownloadedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertDownloadedArticle(DownloadsModel downloaded);

    @Query("DELETE FROM downloaded_tbl")
    Completable deleteAll();

    @Query("SELECT * FROM downloaded_tbl")
    Observable<List<DownloadsModel>> getAllDownloaded();

    @Query("SELECT * FROM downloaded_tbl WHERE idR=:id")
    Observable<DownloadsModel> getDownloaded(int id);

    @Query("DELETE FROM downloaded_tbl WHERE path=:path")
    Completable deleteDownloaded(String path);

    @Query("SELECT * FROM downloaded_tbl WHERE path=:path")
    Observable<List<DownloadsModel>> getDownloadedByPath(String path);

}
