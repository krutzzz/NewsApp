package com.example.samprojre;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;

import com.example.samprojre.R;

import dagger.hilt.android.HiltAndroidApp;
import io.realm.Realm;
import io.realm.RealmConfiguration;

@HiltAndroidApp
public class Singleton extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }


}
