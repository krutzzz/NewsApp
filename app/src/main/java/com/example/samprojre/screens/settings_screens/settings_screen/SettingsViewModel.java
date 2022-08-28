package com.example.samprojre.screens.settings_screens.settings_screen;

import android.util.Log;

import androidx.datastore.preferences.core.Preferences;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.samprojre.data.model.LoadingState;
import com.example.samprojre.data.repository.DataStoreRepository;
import com.example.samprojre.base_adapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class SettingsViewModel extends ViewModel implements OnItemClickListener {

    private final DataStoreRepository dataStoreRepository;

    private final MutableLiveData<LoadingState> _isLoading = new MutableLiveData<>();
    public LiveData<LoadingState> isLoading = _isLoading;

    private final MutableLiveData<List<SourceModel>> _sources = new MutableLiveData<>();
    public LiveData<List<SourceModel>> sources = _sources;

    private final MutableLiveData<String> _countryName = new MutableLiveData<>();
    public LiveData<String> countryName = _countryName;

    private final MutableLiveData<String> _categoryName = new MutableLiveData<>();
    public LiveData<String> categoryName = _categoryName;

    public MutableLiveData<Boolean> countryAndCategoryChecked = new MutableLiveData<>();
    public MutableLiveData<Boolean> sourceChecked = new MutableLiveData<>();

    private boolean enableItemClick = true;

    private String sourcesNames;

    private String errorMessage;

    private Disposable saveSourceDisposable;
    private Disposable sourcesDisposable;
    private Disposable countryDisposable;
    private Disposable categoryDisposable;

    @Inject
    public SettingsViewModel(DataStoreRepository dataStoreRepository, SavedStateHandle savedStateHandle) {
        this.dataStoreRepository = dataStoreRepository;

    }

    public void getCountryAndCategorySettings() {
        categoryDisposable = dataStoreRepository.getCategoryFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        _categoryName::postValue,
                        throwable -> {
                            _categoryName.postValue("general");
                        });

        countryDisposable = dataStoreRepository.getCountryFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(_countryName::postValue, throwable -> {
                    _countryName.postValue("us");
                });
    }

    public void getSourcesSettings() {
        sourcesDisposable = dataStoreRepository.getSourceFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        source -> {
                            sourcesNames = source;
                            List<SourceModel> list = createSourceList(source);
                            _sources.postValue(list);
                        },
                        throwable -> {
                            _sources.postValue(Collections.emptyList());
                        });
    }

    public void getSettings() {
        sourceChecked.postValue(dataStoreRepository.getSourceEnabled());
        countryAndCategoryChecked.postValue(dataStoreRepository.getCountryAndCategoryEnabled());
    }

    public void saveSourceSetting(boolean value) {
        dataStoreRepository.setSourceEnabled(value);
    }

    public void saveCountryAndCategorySetting(boolean value) {
        dataStoreRepository.setCountryAndCategoryEnabled(value);
    }

    private List<SourceModel> createSourceList(String value) {
        ArrayList<SourceModel> list = new ArrayList<>();
        if (value.equals(""))
            return Collections.emptyList();
        String[] stringArr = value.split("; ");
        for (String sourceName : stringArr) {
            SourceModel sourceModel
                    = new SourceModel(sourceName,
                    sourceChecked.getValue() != null ? sourceChecked.getValue() : false);
            list.add(sourceModel);
        }
        return list;
    }




    private SingleObserver<Preferences> getItemRemovalSingleObserver() {
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

            }

            @Override
            public void onError(@NonNull Throwable e) {
                errorMessage = e.getMessage();
                _isLoading.postValue(LoadingState.ERROR);

            }
        };
    }

    @Override
    public void onItemClick(Object item, int position) {
        if (enableItemClick) {
            SourceModel sourceModel = (SourceModel) item;
            if (sourcesNames != null) {
                String newData = sourcesNames.replace(sourceModel.getSourceName() + "; ", "");
                dataStoreRepository.setSources(newData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getItemRemovalSingleObserver());
            }
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(saveSourceDisposable!=null){
            saveSourceDisposable.dispose();
        }
        if (sourcesDisposable != null)
            sourcesDisposable.dispose();
        if (countryDisposable != null)
            countryDisposable.dispose();
        if (categoryDisposable != null)
            categoryDisposable.dispose();

    }
}
