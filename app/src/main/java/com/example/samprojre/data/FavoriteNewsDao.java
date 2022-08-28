package com.example.samprojre.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.samprojre.data.model.FavoritesModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

@Dao
public interface FavoriteNewsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertFavoriteArticle(FavoritesModel favorites);

    @Query("DELETE FROM favorites_tbl")
    Completable deleteAll();

    @Query("SELECT * FROM favorites_tbl")
    Observable<List<FavoritesModel>> getAllFavorites();

    @Query("SELECT * FROM favorites_tbl WHERE idR=:id")
    Observable<FavoritesModel> getFavorite(int id);

    @Query("DELETE FROM favorites_tbl WHERE idR=:id")
    Completable deleteFavorite(int id);

    @Query("SELECT * FROM favorites_tbl WHERE url=:url")
    Observable<List<FavoritesModel>> getFavoriteByTitleAndUrl( String url);
}
