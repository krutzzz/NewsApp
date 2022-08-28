package com.example.samprojre.screens.downloaded_screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.samprojre.base_adapter.OnItemClickListener;
import com.example.samprojre.data.model.DownloadsModel;
import com.example.samprojre.data.model.FavoritesModel;
import com.example.samprojre.data.repository.DownloadedRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class DownloadsViewModel extends ViewModel implements OnItemClickListener {

    private final DownloadedRepository repository;

    private final MutableLiveData<List<DownloadsModel>> _downloadsList
            = new MutableLiveData<>();
    public LiveData<List<DownloadsModel>> downloadsList = _downloadsList;

    private final MutableLiveData<HashMap<Integer, DownloadsModel>> _itemClicked
            = new MutableLiveData<>();
    public LiveData<HashMap<Integer, DownloadsModel>> itemClicked = _itemClicked;

    private Map<Integer, DownloadsModel> toDeleteItem;

    private Disposable downloadsDisposable;

    @Inject
    public DownloadsViewModel(DownloadedRepository repository) {
        this.repository = repository;
        getDownloadsList();
    }

    private void getDownloadsList() {
        repository.getAllDownloaded()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(downloadsObserver);
    }

    public void deleteFavorite(DownloadsModel itemToDelete) {
        repository.deleteDownloaded(itemToDelete.getPath())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void setToDeleteItem(int position) {
        Map<Integer, DownloadsModel> deleteHashmap
                = Collections.singletonMap(position, downloadsList.getValue().get(position));
        this.toDeleteItem = deleteHashmap;
        _downloadsList.getValue().remove(position);
    }

    public void procedeDeletingItem() {
        deleteFavorite((DownloadsModel) toDeleteItem.values().toArray()[0]);
    }

    public void cancelDeleting() {
        int pos = (Integer) Objects.requireNonNull(toDeleteItem.keySet().toArray()[0]);
        DownloadsModel item = (DownloadsModel) toDeleteItem.values().toArray()[0];
        Objects.requireNonNull(_downloadsList.getValue()).add(pos, item);
    }

    @Override
    public void onItemClick(Object item, int position) {
        DownloadsModel clickedItem = (DownloadsModel) item;
        HashMap<Integer, DownloadsModel> hashMap = new HashMap<Integer, DownloadsModel>(position) {
            {
                put(position, clickedItem);
            }
        };
        _itemClicked.postValue(hashMap);
    }


    private final Observer<List<DownloadsModel>> downloadsObserver = new Observer<List<DownloadsModel>>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            downloadsDisposable = d;
//            _isLoading.setValue(LoadingState.LOADING);
            //start showing progress bar
        }

        @Override
        public void onNext(@NonNull List<DownloadsModel> downloadsList) {
            _downloadsList.postValue(downloadsList);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            _downloadsList.postValue(Collections.emptyList());
        }

        @Override
        public void onComplete() {
            //hide progress bar
//            _isLoading.setValue(LoadingState.FINISHED);
        }
    };


    @Override
    protected void onCleared() {
        super.onCleared();
        if (downloadsDisposable != null) {
            downloadsDisposable.dispose();
        }
    }


}
