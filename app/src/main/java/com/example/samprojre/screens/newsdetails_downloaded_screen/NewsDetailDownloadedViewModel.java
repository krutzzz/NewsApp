package com.example.samprojre.screens.newsdetails_downloaded_screen;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.samprojre.data.repository.DataStoreRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NewsDetailDownloadedViewModel extends ViewModel {

    private final SavedStateHandle savedStateHandle;
    private final DataStoreRepository dataStoreRepository;

    private final String source;
    private final String path;
    private final String imgFilePath;
    private final String title;
    private final String date;
    private final String rawAuthor;
    private final String formattedAuthor;
    private final String desc;


    @Inject
    public NewsDetailDownloadedViewModel(DataStoreRepository dataStoreRepository, SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        this.dataStoreRepository = dataStoreRepository;
        source = savedStateHandle.get("source");
        path = savedStateHandle.get("path");
        title = savedStateHandle.get("title");
        date = savedStateHandle.get("date");
        rawAuthor = savedStateHandle.get("author");
        imgFilePath = savedStateHandle.get("imgFilePath");
        desc = savedStateHandle.get("desc");

        if (rawAuthor == null) {
            formattedAuthor = "";
        } else {
            formattedAuthor = "\n" + "\u2022 " + rawAuthor;
        }
    }

    public int getTheme() {
        return dataStoreRepository.getTheme();
    }

    public String getImg() {
        return imgFilePath;
    }

    public String getImgFilePath() {
        return imgFilePath;
    }

    public String getPath() {
        return path;
    }

    public String getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getFormattedAuthor() {
        return formattedAuthor;
    }

    public String getDesc() {
        return desc;
    }


}
