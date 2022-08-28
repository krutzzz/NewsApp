package com.example.samprojre.data;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import com.example.samprojre.R;

import javax.inject.Inject;


public class Datastore {

    //    private static Datastore INSTANCE;
    private final Context context;
    private RxDataStore<Preferences> rxDataStore;
    private final Preferences.Key<String> COUNTRY_VALUE_KEY = PreferencesKeys.stringKey("country");
    private final Preferences.Key<String> CATEGORY_VALUE_KEY = PreferencesKeys.stringKey("category");
    private final Preferences.Key<String> SOURCE_VALUE_KEY = PreferencesKeys.stringKey("source");
    private final Preferences.Key<Boolean> SOURCE_ENABLED_VALUE_KEY = PreferencesKeys.booleanKey("sourceEnabled");
    private final Preferences.Key<Boolean> COUNTRY_AND_CATEGORY_VALUE_KEY = PreferencesKeys.booleanKey("countryAndCategoryEnabled");
    private final String THEME_KEY = "theme";
    private final String SHAREDPREFERENCES_THEME_KEY = "SharedPreferences theme";


    private final String COUNTRY_AND_CATEGORY_ENABLED_KEY = "country and category";
    private final String SOURCES_ENABLED_KEY = "sources";
    private final String SHAREDPREFERENCES_SETTINGS_KEY = "SharedPreferences settings";

    public Preferences.Key<String> getCOUNTRY_VALUE_KEY() {
        return COUNTRY_VALUE_KEY;
    }

    public Preferences.Key<String> getCATEGORY_VALUE_KEY() {
        return CATEGORY_VALUE_KEY;
    }

    public Preferences.Key<String> getSOURCE_VALUE_KEY() {
        return SOURCE_VALUE_KEY;
    }

    public Preferences.Key<Boolean> getCOUNTRY_AND_CATEGORY_VALUE_KEY() {
        return COUNTRY_AND_CATEGORY_VALUE_KEY;
    }

    public Preferences.Key<Boolean> getSOURCE_ENABLED_VALUE_KEY() {
        return SOURCE_ENABLED_VALUE_KEY;
    }

    @Inject
    public Datastore(Context context) {
        this.context = context;
        createRxDataStore(context);
    }

    public int getTheme() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCES_THEME_KEY, MODE_PRIVATE);
        return sharedPreferences.getInt(THEME_KEY, R.style.AppThemeBlue);
    }

    public void saveTheme(int theme) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCES_THEME_KEY, MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt(THEME_KEY, theme);
        ed.apply();
    }

    public void setCountryAndCategoryEnabled(boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCES_SETTINGS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(COUNTRY_AND_CATEGORY_ENABLED_KEY, value);
        ed.apply();
    }

    public boolean getSourceEnabled(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCES_SETTINGS_KEY, MODE_PRIVATE);
        return  sharedPreferences.getBoolean(SOURCES_ENABLED_KEY, false);
    }

    public void setSourceEnabled(boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCES_SETTINGS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(SOURCES_ENABLED_KEY, value);
        ed.apply();
    }

    public boolean getCountryAndCategoryEnabled(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCES_SETTINGS_KEY, MODE_PRIVATE);
        return  sharedPreferences.getBoolean(COUNTRY_AND_CATEGORY_ENABLED_KEY, false);
    }


    public RxDataStore<Preferences> getRxDataStore() {
        return rxDataStore;
    }

    private void createRxDataStore(Context context) {
        this.rxDataStore = new RxPreferenceDataStoreBuilder(context, "settings").build();
    }


}
