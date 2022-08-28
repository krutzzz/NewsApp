package com.example.samprojre.screens.settings_screens.addsource_screen;

import androidx.datastore.preferences.core.Preferences;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.samprojre.base_adapter.OnItemClickListener;
import com.example.samprojre.data.model.LoadingState;
import com.example.samprojre.data.model.newsapi.News;
import com.example.samprojre.data.model.newsapi.Source;
import com.example.samprojre.data.repository.DataStoreRepository;
import com.example.samprojre.data.repository.SourcesRepository;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class SourcesViewModel extends ViewModel implements OnItemClickListener {

    private final SourcesRepository sourcesRepository;
    private final DataStoreRepository dataStoreRepository;

    private final MutableLiveData<LoadingState> _isLoading = new MutableLiveData<>();
    public LiveData<LoadingState> isLoading = _isLoading;

    private final MutableLiveData<String> _addedItem = new MutableLiveData<>();
    public LiveData<String> addedItem = _addedItem;

    private final MutableLiveData<List<Source>> _sources = new MutableLiveData<>();
    public LiveData<List<Source>> sources = _sources;

    public MutableLiveData<Boolean> autoCompleteCountryState = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> autoCompleteCategoryState = new MutableLiveData<>(true);

    private String listOfSources;

    private List<String> listOfSourcesInList;

    private String errorMessage;

    private boolean enableItemClick = true;
    private String sourceName;

    private Disposable sourcesDisposable;
    private Disposable saveSourceDisposable;
    private Disposable savedSourceDisposable;

    @Inject
    public SourcesViewModel(SourcesRepository sourcesRepository, DataStoreRepository dataStoreRepository) {
        this.sourcesRepository = sourcesRepository;
        this.dataStoreRepository = dataStoreRepository;
        getSavedSources();
    }

    private void getSavedSources() {
        savedSourceDisposable = dataStoreRepository.getSourceFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        savedSource -> {
                            listOfSources = savedSource;
                            listOfSourcesInList = Arrays.asList(listOfSources.split("; "));
                        },
                        throwable -> {
                            listOfSources = "";
                        }

                );
    }

    public void getSources(String country, String category) {
        sourcesRepository.getSources(country, category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private void setSource() {
        if (!listOfSources.contains(sourceName)) {
            listOfSources = listOfSources + sourceName + "; ";
            dataStoreRepository.setSources(listOfSources).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getSingleObserver());
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private Observer<News> getObserver() {
        return new Observer<News>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                sourcesDisposable = d;
                _isLoading.postValue(LoadingState.LOADING);
            }

            @Override
            public void onNext(@NonNull News news) {
                _sources.postValue(news.getSourcesList());
                _isLoading.postValue(LoadingState.FINISHED);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                _isLoading.postValue(LoadingState.ERROR);
            }

            @Override
            public void onComplete() {
                _isLoading.postValue(LoadingState.FINISHED);
            }
        };
    }

    private SingleObserver<Preferences> getSingleObserver() {
        return new SingleObserver<Preferences>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                enableItemClick = false;
                _isLoading.postValue(LoadingState.LOADING);
                saveSourceDisposable = d;
            }

            @Override
            public void onSuccess(@NonNull Preferences preferences) {
                enableItemClick = true;
                _isLoading.postValue(LoadingState.FINISHED);
                _addedItem.postValue(sourceName);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                errorMessage = e.getMessage();
                _isLoading.postValue(LoadingState.ERROR);

            }
        };
    }

    public List<String> getListOfSourcesInList() {
        return listOfSourcesInList;
    }


    @Override
    public void onItemClick(Object item, int position) {
        if (enableItemClick) {
            listOfSourcesInList = Arrays.asList(listOfSources.split("; "));
            if (listOfSourcesInList.size() < 20) {
                Source sourceItem = (Source) item;
                sourceName = sourceItem.getName();
                setSource();
            } else {
                errorMessage = "Max amount of sources reached(20)!";
                _isLoading.postValue(LoadingState.ERROR);
            }
        }
    }


    public Disposable getSourcesDisposable() {
        return sourcesDisposable;
    }

    public Disposable getSaveSourceDisposable() {
        return saveSourceDisposable;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (sourcesDisposable != null) {
            sourcesDisposable.dispose();
        }
        if (saveSourceDisposable != null) {
            saveSourceDisposable.dispose();
        }
        if (savedSourceDisposable != null) {
            savedSourceDisposable.dispose();
        }

    }
}
