package com.example.samprojre.screens.settings_screens.choose_category_or_country_screen;

import com.example.samprojre.base_adapter.ListAdapterItem;
import com.example.samprojre.utils.diffutil.DiffUtilEquality;

import org.jetbrains.annotations.Nullable;

public class CountryCategoryModel implements ListAdapterItem, DiffUtilEquality {

    private String name;
    private boolean chosen;

    @Nullable
    private String countryCode;

    public CountryCategoryModel(String name, String countryCode, boolean chosen) {
        this.name = name;
        this.chosen = chosen;
        this.countryCode = countryCode;
    }

    public CountryCategoryModel(String name, boolean chosen) {
        this.name = name;
        this.chosen = chosen;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public boolean realEquals(Object toCompare) {
        if (this == toCompare) return true;
        if (toCompare == null || getClass() != toCompare.getClass()) return false;
        CountryCategoryModel item = (CountryCategoryModel) toCompare;
        return name.equals(item.getName()) && chosen == item.isChosen();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
