package com.example.samprojre.screens.newsdetails_screen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.samprojre.data.repository.DataStoreRepository;
import com.example.samprojre.data.repository.DownloadedRepository;
import com.example.samprojre.data.model.DownloadsModel;
import com.example.samprojre.data.model.LoadingState;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

@HiltViewModel
public class NewsDetailViewModel extends ViewModel {


    private final DataStoreRepository dataStoreRepository;
    private final DownloadedRepository repository;
    private final SavedStateHandle savedStateHandle;

    public final MutableLiveData<LoadingState> _isLoading = new MutableLiveData<>();
    public LiveData<LoadingState> isLoading = _isLoading;

    private final MutableLiveData<String> _path = new MutableLiveData<>();
    public LiveData<String> path = _path;

    private final String source;
    private final String url;
    private final String urlToImg;
    private final String img;
    private final String title;
    private final String date;
    private final String rawAuthor;
    private final String formattedAuthor;
    private final String desc;
    //    private byte[] byteArray;
    private String imgFilePath;

    public String getSource() {
        return source;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImg() {
        return urlToImg;
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getRawAuthor() {
        return rawAuthor;
    }

    public String getFormattedAuthor() {
        return formattedAuthor;
    }

    private Disposable saveTheArticleWebPageDisposable;
    private Disposable saveTheArticleImgDisposable;

    @Inject
    public NewsDetailViewModel(DownloadedRepository repository, DataStoreRepository dataStoreRepository, SavedStateHandle savedStateHandle) {
        this.repository = repository;
        this.savedStateHandle = savedStateHandle;
        this.dataStoreRepository = dataStoreRepository;

        source = savedStateHandle.get("source");
        url = savedStateHandle.get("url");
        title = savedStateHandle.get("title");
        img = savedStateHandle.get("img");
        date = savedStateHandle.get("date");
        rawAuthor = savedStateHandle.get("author");
        urlToImg = savedStateHandle.get("urlToImg");
        desc = savedStateHandle.get("desc");

        if (rawAuthor == null) {
            formattedAuthor = "";
        } else {
            formattedAuthor = "\n" + "\u2022 " + rawAuthor;
        }

    }

    public int getTheme(){
        return dataStoreRepository.getTheme();
    }

    public void getBitmapFromUrl() {
        Observable<ResponseBody> response = repository.downloadImage(urlToImg);
        response.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getDownloadObserver());
    }

    public void insertDownloaded(String path) {
        DownloadsModel downloadedArticle = new DownloadsModel();
        downloadedArticle.setTitle(title);
        downloadedArticle.setDescription(desc);
        downloadedArticle.setSource(source);
        downloadedArticle.setPath(path);
        downloadedArticle.setImgFilePath(imgFilePath);
        downloadedArticle.setPublishedAt(date);
        downloadedArticle.setAuthor(rawAuthor);

        repository.insertDownloaded(downloadedArticle)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getCompletableObserver());
    }

    public void deleteDownloaded() {
        repository.deleteDownloaded(_path.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getCompletableObserver());
    }

    private CompletableObserver getCompletableObserver() {
        return new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                saveTheArticleWebPageDisposable = d;
                _isLoading.setValue(LoadingState.LOADING);
            }

            @Override
            public void onComplete() {
                _isLoading.setValue(LoadingState.FINISHED);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                _isLoading.setValue(LoadingState.ERROR);
            }
        };
    }

    private Observer<ResponseBody> getDownloadObserver() {
        return new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                _isLoading.postValue(LoadingState.LOADING);
            }

            @Override
            public void onNext(@NonNull ResponseBody responseBody) {
                InputStream is = responseBody.byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                String fileName = String.valueOf(System.currentTimeMillis());
                onSavePng(fileName + "1", bitmap);


                _path.postValue(fileName);

            }

            @Override
            public void onError(@NonNull Throwable e) {
                _isLoading.postValue(LoadingState.ERROR);
            }

            @Override
            public void onComplete() {

            }
        };
    }

    public void onSavePng(String currentName, Bitmap bitmap) {
        try {
            String root = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString();
            File myDir = new File(root + "/saved_images");
            myDir.mkdirs();
            String fname = currentName + ".png";
            File file = new File(myDir, fname);

            FileOutputStream out = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            imgFilePath = file.getAbsolutePath();
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.d("onBtnSavePng", e.toString());
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (saveTheArticleImgDisposable != null) {
            saveTheArticleImgDisposable.dispose();
        }
        if (saveTheArticleWebPageDisposable != null) {
            saveTheArticleWebPageDisposable.dispose();
        }
    }
}
