package com.example.samprojre.screens.settings_screens.choose_category_or_country_screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.samprojre.base_adapter.OnItemClickListener;
import com.example.samprojre.data.repository.DataStoreRepository;
import com.example.samprojre.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class ChooseCountryOrCategoryViewModel extends ViewModel implements OnItemClickListener {

    private final DataStoreRepository repository;


    private final MutableLiveData<String> _title = new MutableLiveData<>();
    public LiveData<String> title = _title;

    private final MutableLiveData<Boolean> _settingsLoaded = new MutableLiveData<>();
    public LiveData<Boolean> settingsLoaded = _settingsLoaded;

    private final MutableLiveData<List<CountryCategoryModel>> _list = new MutableLiveData<>();
    public LiveData<List<CountryCategoryModel>> list = _list;

    private String countrySelectedValue;
    private String categorySelectedValue;

    private Disposable categoryDisposable;
    private Disposable countryDisposable;
    private Disposable saveCountryOrCategoryDisposable;

    @Inject
    public ChooseCountryOrCategoryViewModel(DataStoreRepository dataStoreRepository, SavedStateHandle savedStateHandle) {
        this.repository = dataStoreRepository;
        String type = savedStateHandle.get("title");
        _title.postValue(type);

        getSettings(type != null ? type : "Category");
    }

    private void getSettings(String type) {
        if (type.equals("Category")) {
            categoryDisposable = repository.getCategoryFlowable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .take(1)
                    .subscribe(
                            chosenCategory -> {
                                categorySelectedValue = chosenCategory;
                                _settingsLoaded.postValue(true);
                            },
                            error -> {
                                categorySelectedValue = "general";
                                _settingsLoaded.postValue(true);
                            }
                    );
        }
        if (type.equals("Country")) {
            countryDisposable = repository.getCountryFlowable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .take(1)
                    .subscribe(
                            chosenCountry -> {
                                countrySelectedValue = chosenCountry;
                                _settingsLoaded.postValue(true);
                            },
                            error -> {
                                countrySelectedValue = Utils.getCountry();
                                _settingsLoaded.postValue(true);
                            }
                    );
        }
    }

    public void setCountries(String[] countryNames, String[] countryCodes) {
        ArrayList<CountryCategoryModel> countryList = new ArrayList<>();
        for (int i = 0; i < countryNames.length; i++) {
            CountryCategoryModel countryCategoryItem;
            if (countryCodes[i].equals(countrySelectedValue)) {
                countryCategoryItem = new CountryCategoryModel(countryNames[i], countryCodes[i], true);
            } else {
                countryCategoryItem = new CountryCategoryModel(countryNames[i], countryCodes[i], false);
            }
            countryList.add(countryCategoryItem);

        }
        _list.postValue(countryList);
    }


    public void setCategories(String[] categories) {
        ArrayList<CountryCategoryModel> categoryList = new ArrayList<>();
        for (String category : categories) {
            CountryCategoryModel countryCategoryItem;
            if (category.equals(categorySelectedValue)) {
                countryCategoryItem = new CountryCategoryModel(category, true);
            } else {
                countryCategoryItem = new CountryCategoryModel(category, false);
            }
            categoryList.add(countryCategoryItem);

        }
        _list.postValue(categoryList);

    }

    private void updateItemList(int position) {
        List<CountryCategoryModel> referenceToOriginalList = new ArrayList<>(Objects.requireNonNull(list.getValue()));
        List<CountryCategoryModel> updatedList = new ArrayList<>();
        for (int i = 0; i < referenceToOriginalList.size(); i++) {
            CountryCategoryModel item = referenceToOriginalList.get(i);
            if (i == position) {
                item.setChosen(true);
            } else {
                item.setChosen(false);
            }
            updatedList.add(item);
        }
        _list.postValue(updatedList);
    }

    private void saveCountry(String countryCode) {
        saveCountryOrCategoryDisposable = repository.setCountry(countryCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    private void saveCategory(String name) {
        saveCountryOrCategoryDisposable = repository.setCategory(name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void onItemClick(Object item, int position) {
        CountryCategoryModel countryCategoryItem = (CountryCategoryModel) item;
        if (!((CountryCategoryModel) item).isChosen()) {
            if (Objects.equals(_title.getValue(), "Country")) {
                saveCountry(countryCategoryItem.getCountryCode());
            } else {
                saveCategory(countryCategoryItem.getName());
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (countryDisposable != null)
            countryDisposable.dispose();

        if (categoryDisposable != null)
            categoryDisposable.dispose();

        if (saveCountryOrCategoryDisposable != null)
            saveCountryOrCategoryDisposable.dispose();
    }

}
