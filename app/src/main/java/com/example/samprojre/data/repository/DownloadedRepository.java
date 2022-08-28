package com.example.samprojre.data.repository;

import com.example.samprojre.api.requests.ApiNewsRequests;
import com.example.samprojre.data.DownloadedDao;
import com.example.samprojre.data.model.DownloadsModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;


public class DownloadedRepository {

    private final DownloadedDao downloadedDao;
    private final ApiNewsRequests apiNewsRequests;

    @Inject
    public DownloadedRepository(DownloadedDao downloadedDao, ApiNewsRequests apiNewsRequests) {
        this.downloadedDao = downloadedDao;
        this.apiNewsRequests = apiNewsRequests;
    }

    public Observable<List<DownloadsModel>> getAllDownloaded() {
        return downloadedDao.getAllDownloaded();
    }

    public Observable<DownloadsModel> getDownloadedById(int id) {
        return downloadedDao.getDownloaded(id);
    }

    public Observable<List<DownloadsModel>> getDownloadedPath(String path) {
        return downloadedDao.getDownloadedByPath(path);
    }

    public Observable<ResponseBody> downloadImage(String url){
        return apiNewsRequests.downloadImage(url);
    }

    public Completable insertDownloaded(DownloadsModel downloadsModel) {
        return downloadedDao.insertDownloadedArticle(downloadsModel);
    }

    public Completable deleteAll() {
        return downloadedDao.deleteAll();
    }

    public Completable deleteDownloaded(String path) {
        return downloadedDao.deleteDownloaded(path);
    }


}
