package com.example.samprojre.screens.settings_screens;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.samprojre.data.repository.DataStoreRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ViewModelSettingsActivity extends ViewModel {

    private DataStoreRepository dataStoreRepository;

    @Inject
    public ViewModelSettingsActivity(DataStoreRepository dataStoreRepository, SavedStateHandle savedStateHandle){
        this.dataStoreRepository = dataStoreRepository;
    }

    public int getTheme(){
        return dataStoreRepository.getTheme();
    }


}
