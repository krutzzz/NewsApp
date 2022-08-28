package com.example.samprojre.screens.main;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.samprojre.data.repository.DataStoreRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainActivityViewModel extends ViewModel {

    private SavedStateHandle savedStateHandle;
    private DataStoreRepository dataStoreRepository;

    @Inject
    public MainActivityViewModel(SavedStateHandle savedStateHandle, DataStoreRepository dataStoreRepository) {
        this.savedStateHandle = savedStateHandle;
        this.dataStoreRepository = dataStoreRepository;

    }

    public void setTheme(int theme){
        dataStoreRepository.saveTheme(theme);
    }

    public int getTheme() {
        return dataStoreRepository.getTheme();
    }
}
