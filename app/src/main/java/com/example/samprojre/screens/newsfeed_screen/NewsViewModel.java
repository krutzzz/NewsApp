package com.example.samprojre.screens.newsfeed_screen;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.samprojre.base_adapter.OnArticleItemClickListener;
import com.example.samprojre.data.model.FavoritesModel;
import com.example.samprojre.data.model.LoadingState;
import com.example.samprojre.data.model.newsapi.Article;
import com.example.samprojre.data.model.newsapi.News;
import com.example.samprojre.data.repository.DataStoreRepository;
import com.example.samprojre.data.repository.FavoritesRepository;
import com.example.samprojre.data.repository.NewsRepository;
import com.example.samprojre.utils.Utils;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
public class NewsViewModel extends ViewModel implements OnArticleItemClickListener {

    private final MutableLiveData<News> _newsFeedList = new MutableLiveData<>();
    public LiveData<News> newsFeedList = _newsFeedList;

    private final MutableLiveData<LoadingState> _isLoading = new MutableLiveData<>();
    public LiveData<LoadingState> isLoading = _isLoading;

    //Need to have it public in order to set the state of it in NewsFeed,
    public final MutableLiveData<Boolean> _optionsClicked = new MutableLiveData<>(false);
    public LiveData<Boolean> optionsClicked = _optionsClicked;

    private final MutableLiveData<FavoritesModel> _optionsFavoriteState = new MutableLiveData<>();
    public LiveData<FavoritesModel> optionsFavoriteState = _optionsFavoriteState;

    private final MutableLiveData<Article> _clickedArticle = new MutableLiveData<>();
    public LiveData<Article> clickedArticle = _clickedArticle;

    private final MutableLiveData<LoadingState> _isDatabaseLoading = new MutableLiveData<>();
    public LiveData<LoadingState> isDatabaseLoading = _isDatabaseLoading;

    private final MutableLiveData<Integer> _itemClick = new MutableLiveData<>();
    public LiveData<Integer> itemClick = _itemClick;

    private String errorMessage;

    private final NewsRepository newsRepository;
    private final DataStoreRepository dataStoreRepository;
    private final FavoritesRepository favoritesRepository;

    private Disposable newsDisposable;
    private Disposable clickedArticleDisposable;
    private Disposable disposableCountryFlowable;
    private Disposable disposableCategoryFlowable;
    private Disposable disposableSourceFlowable;

    private String country;
    private String category;
    private String sources;
    private String sortBy = "relevancy";


    private boolean sourceEnabled;
    private boolean countryOrCategoryEnabled;


    //I am not sure whether it is a good solution but I will try.
    //1 element is country
    //2 element is category
    //3 element is source
    private final MutableLiveData<List<String>> settings = new MutableLiveData<>(Arrays.asList(null, null, null));

    @Inject
    public NewsViewModel(NewsRepository newsRepository, DataStoreRepository dataStoreRepository, FavoritesRepository favoritesRepository) {
        this.newsRepository = newsRepository;
        this.dataStoreRepository = dataStoreRepository;
        this.favoritesRepository = favoritesRepository;
        getDataFromSettings();
    }

    //For NewsFeed
    public void getNews(@Nullable String searchQuery, int pageSize) {
        Observable<News> response = newsRepository.getNews(sourceEnabled, countryOrCategoryEnabled,
                country, category, sortBy, sources, searchQuery, pageSize);
        response.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsObserver);
    }

    public void setTheme(int theme) {
        dataStoreRepository.saveTheme(theme);
    }

    public int getTheme() {
        return dataStoreRepository.getTheme();
    }

    public void getDataFromSettings() {
        _isLoading.setValue(LoadingState.LOADING);
        getSettings();
        if (sourceEnabled)
            getSources();
        else if (countryOrCategoryEnabled)
            getCountryAndCategorySettings();
        else{
            settings.getValue().set(0,null);
            settings.postValue(settings.getValue());
        }

        settings.observeForever(settingsObserver);
    }

    public void getSettings() {
        if (this.sourceEnabled != dataStoreRepository.getSourceEnabled() || this.countryOrCategoryEnabled != dataStoreRepository.getCountryAndCategoryEnabled()) {

            this.sourceEnabled = dataStoreRepository.getSourceEnabled();
            this.countryOrCategoryEnabled = dataStoreRepository.getCountryAndCategoryEnabled();

            settings.postValue(settings.getValue());
        }

    }

    private void getSources() {
        disposableSourceFlowable =
                dataStoreRepository.getSourceFlowable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                source -> {
                                    Objects.requireNonNull(settings.getValue()).set(2, source);
                                    settings.setValue(settings.getValue());
                                },
                                throwable -> {
                                    Objects.requireNonNull(settings.getValue()).set(2, "");
                                    settings.setValue(settings.getValue());
                                });
    }

    private void getCountryAndCategorySettings() {
        disposableCountryFlowable =
                dataStoreRepository.getCountryFlowable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                countryPref -> {
                                    Objects.requireNonNull(settings.getValue()).set(0, countryPref);
                                    settings.setValue(settings.getValue());
                                },
                                throwable -> {
                                    Objects.requireNonNull(settings.getValue()).set(0, Utils.getCountry());
                                    settings.setValue(settings.getValue());
                                });


        disposableCategoryFlowable =
                dataStoreRepository.getCategoryFlowable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                category -> {
                                    Objects.requireNonNull(settings.getValue()).set(1, category);
                                    settings.setValue(settings.getValue());
                                },
                                throwable -> {
                                    Objects.requireNonNull(settings.getValue()).set(1, "general");
                                    settings.setValue(settings.getValue());
                                });
    }

    private final androidx.lifecycle.Observer<List<String>> settingsObserver = settings -> {
        if (!sourceEnabled && !countryOrCategoryEnabled) {
            country = Utils.getCountry();
            getNews(null, 100);
        } else if (sourceEnabled && settings.get(2) != null) {
            sources = settings.get(2);
            getNews(null, 100);
        } else if (countryOrCategoryEnabled && settings.get(0) != null && settings.get(1) != null) {
            country = settings.get(0);
            category = settings.get(1);
            getNews(null, 100);
        }
    };

    private final Observer<News> newsObserver = new Observer<News>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            newsDisposable = d;
            _isLoading.setValue(LoadingState.LOADING);
            //start showing progress bar
        }

        @Override
        public void onNext(@NonNull News news) {
            _newsFeedList.postValue(news);
            _isLoading.setValue(LoadingState.FINISHED);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            errorMessage = e.getMessage();
            List<Article> list = Collections.emptyList();
            News news = new News();
            news.setArticle(list);
            _newsFeedList.postValue(news);
            _isLoading.setValue(LoadingState.ERROR);
        }

        @Override
        public void onComplete() {
            //hide progress bar
            _isLoading.setValue(LoadingState.FINISHED);
        }
    };

    @Override
    public void onItemClick(Object item, int position) {
        Article article = (Article) item;
        _clickedArticle.setValue(article);
        _itemClick.postValue(position);
        Log.d("NewsFeedViewModel", "onItemClick: Item was clicked");
    }

    @Override
    public void onOptionsClick(Object item) {
        Article article = ((Article) item);
        _clickedArticle.setValue(article);
        clickedArticleDisposable = getFavoriteByTitleAndUrl(article.getUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favoriteArticle -> {
                    if (favoriteArticle.isEmpty()) {
                        _optionsFavoriteState.setValue(null);
                    } else {
                        _optionsFavoriteState.setValue(favoriteArticle.get(0));
                    }
                    _optionsClicked.postValue(true);

                }, throwable -> {
                    _optionsFavoriteState.setValue(null);
                    _optionsClicked.setValue(true);
                });
        Log.d("NewsFeedViewModel", "onOptionsClick: Item was clicked");
    }

    public Observable<List<FavoritesModel>> getFavoriteByTitleAndUrl(String url) {
        return favoritesRepository.getFavoritesByTitleAndUrl(url);
    }

    public void deleteFavorite() {
        clickedArticleDisposable.dispose();
        if (optionsFavoriteState.getValue() != null) {
            favoritesRepository.deleteFavorite(optionsFavoriteState.getValue().getIdR())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getCompletableObserver());
        }
    }

    public void insertFavorite() {
        clickedArticleDisposable.dispose();
        FavoritesModel favoriteArticle = new FavoritesModel();
        if (_clickedArticle.getValue() != null) {
            favoriteArticle.setAuthor(_clickedArticle.getValue().getAuthor());
            favoriteArticle.setSourceName(_clickedArticle.getValue().getSourceName().getName());
            favoriteArticle.setTitle(_clickedArticle.getValue().getTitle());
            favoriteArticle.setDescription(_clickedArticle.getValue().getDescription());
            favoriteArticle.setUrl(_clickedArticle.getValue().getUrl());
            favoriteArticle.setUrlToImage(_clickedArticle.getValue().getUrlToImage());
            favoriteArticle.setPublishedAt(_clickedArticle.getValue().getPublishedAt());
            favoritesRepository.insertFavorites(favoriteArticle)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getCompletableObserver());
        }
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

    @Override
    protected void onCleared() {
        super.onCleared();
        settings.removeObserver(settingsObserver);

        if (disposableCategoryFlowable != null) {
            disposableCategoryFlowable.dispose();
        }
        if (disposableSourceFlowable != null) {
            disposableSourceFlowable.dispose();
        }
        if (disposableCountryFlowable != null) {

            disposableCountryFlowable.dispose();
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
