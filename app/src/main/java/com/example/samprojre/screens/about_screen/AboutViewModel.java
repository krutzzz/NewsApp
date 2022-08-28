package com.example.samprojre.screens.about_screen;

import androidx.lifecycle.ViewModel;

import com.example.samprojre.data.repository.DataStoreRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AboutViewModel extends ViewModel {

    private DataStoreRepository repository;

    @Inject
    public AboutViewModel(DataStoreRepository dataStoreRepository) {
        this.repository = dataStoreRepository;
    }

    public int getTheme(){
        return repository.getTheme();
    }

}
