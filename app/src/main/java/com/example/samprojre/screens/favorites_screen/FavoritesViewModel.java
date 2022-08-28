package com.example.samprojre.screens.favorites_screen;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.samprojre.base_adapter.OnArticleItemClickListener;
import com.example.samprojre.data.model.FavoritesModel;
import com.example.samprojre.data.repository.FavoritesRepository;
import com.example.samprojre.data.model.LoadingState;
import com.example.samprojre.base_adapter.OnItemClickListener;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class FavoritesViewModel extends ViewModel implements OnArticleItemClickListener {

    private final FavoritesRepository repository;

    private final MutableLiveData<FavoritesModel> _clickedArticle = new MutableLiveData<>();
    public LiveData<FavoritesModel> clickedArticle = _clickedArticle;

    private final MutableLiveData<Integer> _itemClick = new MutableLiveData<>();
    public LiveData<Integer> itemClick = _itemClick;

    public final MutableLiveData<Boolean> _optionsClicked = new MutableLiveData<>(false);
    public LiveData<Boolean> optionsClicked = _optionsClicked;

    private final MutableLiveData<List<FavoritesModel>> _favoritesList = new MutableLiveData<>();
    public LiveData<List<FavoritesModel>> favoritesList = _favoritesList;

    private final MutableLiveData<LoadingState> _isDatabaseLoading = new MutableLiveData<>();
    public LiveData<LoadingState> isDatabaseLoading = _isDatabaseLoading;

    private Map<Integer, FavoritesModel> toDeleteItem;


    public Disposable deleteListener;
    public Disposable disposable;


    @Inject
    public FavoritesViewModel(FavoritesRepository favoritesRepository) {
        this.repository = favoritesRepository;
    }

    public void getFavorites() {
        Observable<List<FavoritesModel>> response
                = getFavoritesObservable();
        response.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getFavoritesObserver());
    }

    private Observable<List<FavoritesModel>> getFavoritesObservable() {
        return repository.getAllFavorites();
    }


    public void deleteFavorite() {
        if (clickedArticle != null) {
            repository.deleteFavorite(clickedArticle.getValue().getIdR())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getCompletableObserver());
        }
    }

    public void deleteFavorite(FavoritesModel itemToDelete) {
        repository.deleteFavorite(itemToDelete.getIdR())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getCompletableObserver());
    }

    public void procedeDeletingItem() {
        deleteFavorite((FavoritesModel) toDeleteItem.values().toArray()[0]);
    }

    public void setToDeleteItem(int position) {
        Map<Integer, FavoritesModel> deleteHashmap
                = Collections.singletonMap(position, favoritesList.getValue().get(position));
        this.toDeleteItem=deleteHashmap;
        _favoritesList.getValue().remove(position);
    }


    public void cancelDeleting() {
        int pos = (Integer) Objects.requireNonNull(toDeleteItem.keySet().toArray()[0]);
        FavoritesModel item = (FavoritesModel) toDeleteItem.values().toArray()[0];
        Objects.requireNonNull(_favoritesList.getValue()).add(pos, item);
    }


    private CompletableObserver getCompletableObserver() {
        return new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                _isDatabaseLoading.setValue(LoadingState.LOADING);
            }

            @Override
            public void onComplete() {
                _isDatabaseLoading.setValue(LoadingState.FINISHED);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                _isDatabaseLoading.setValue(LoadingState.ERROR);
            }
        };
    }

    private Observer<List<FavoritesModel>> getFavoritesObserver() {
        return new Observer<List<FavoritesModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable = d;
                _isDatabaseLoading.setValue(LoadingState.LOADING);
                //start showing progress bar
            }

            @Override
            public void onNext(@NonNull List<FavoritesModel> favorites) {
                _favoritesList.postValue(favorites);
                _isDatabaseLoading.setValue(LoadingState.FINISHED);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                _favoritesList.postValue(Collections.emptyList());
                _isDatabaseLoading.setValue(LoadingState.ERROR);
            }

            @Override
            public void onComplete() {
                //hide progress bar
                _isDatabaseLoading.setValue(LoadingState.FINISHED);
            }
        };
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }


    @Override
    public void onItemClick(Object item, int position) {
        FavoritesModel favoritesModel = (FavoritesModel) item;
        _clickedArticle.postValue(favoritesModel);
        _itemClick.postValue(position);
    }

    @Override
    public void onOptionsClick(Object item) {
        FavoritesModel clickedItem = (FavoritesModel) item;
        _clickedArticle.setValue(clickedItem);
        _optionsClicked.postValue(true);
        Log.d("FavoritesViewModel", "onOptionsClick: Item was clicked");
    }


}
