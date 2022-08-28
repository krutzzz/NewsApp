package com.example.samprojre.data.repository;

import com.example.samprojre.data.FavoriteNewsDao;
import com.example.samprojre.data.model.FavoritesModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public class FavoritesRepository {

    private final FavoriteNewsDao favoriteNewsDao;


    @Inject
    public FavoritesRepository(FavoriteNewsDao favoriteNewsDao) {
        this.favoriteNewsDao = favoriteNewsDao;
    }

    public Observable<List<FavoritesModel>> getAllFavorites(){
        return favoriteNewsDao.getAllFavorites();
    }

    public Observable<FavoritesModel> getFavoriteById(int id){
        return favoriteNewsDao.getFavorite(id);
    }

    public Observable<List<FavoritesModel>> getFavoritesByTitleAndUrl( String url){
        return favoriteNewsDao.getFavoriteByTitleAndUrl(url);
    }

    public Completable insertFavorites(FavoritesModel favoritesModel){
        return favoriteNewsDao.insertFavoriteArticle(favoritesModel);
    }

    public Completable deleteAll(){
        return favoriteNewsDao.deleteAll();
    }

    public Completable deleteFavorite(int id){
        return favoriteNewsDao.deleteFavorite(id);
    }




}
