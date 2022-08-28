package com.example.samprojre.data.repository;

import com.example.samprojre.api.requests.ApiNewsRequests;
import com.example.samprojre.data.model.newsapi.News;
import com.example.samprojre.utils.Constants;
import com.example.samprojre.utils.Utils;

import org.jetbrains.annotations.Nullable;

import io.reactivex.rxjava3.core.Observable;

public class NewsRepository {

    private final ApiNewsRequests apiNewsRequests;

    public NewsRepository(ApiNewsRequests apiNewsRequests) {
        this.apiNewsRequests = apiNewsRequests;
    }

    public Observable<News> getNews(
            boolean sourceEnabled,
            boolean countryOrCategoryEnabled,
            String country,
            String category,
            String sortBy,
            String sources,
            @Nullable String searchQuery,
            int pageSize) {

        String language = Utils.getLanguage();
        String allSources = "";
        if (sources != null) {
            allSources = sources.replaceAll("; ", ",");
            allSources = allSources.substring(0,allSources.length()-1);
        }
        if (searchQuery != null) {
            return getNewsSearch(searchQuery, language, sortBy);
        } else {
            if (countryOrCategoryEnabled) {
                return getNewsCategoryCountry(category, country, pageSize);
            }
            if (sourceEnabled && !allSources.equals("")) {
                return getNewsBySource(allSources, pageSize);
            }
//            if (!category.equals("")) {
//                return getNewsCategory(category, pageSize);
//            }
            if (country == null) {
                country = Utils.getCountry();
            }
            return getNewsDefault(country, pageSize);
        }

    }

    public Observable<News> getNewsSearch(String keyword, String language, String sortBy) {
        return apiNewsRequests
                .getNewsSearch(keyword, language, sortBy, Constants.API_KEY);
    }

    private Observable<News> getNewsDefault(String country, int pageSize) {
        return apiNewsRequests
                .getNewsDefault(country, Constants.API_KEY, pageSize);
    }

    private Observable<News> getNewsBySource(String sources, int pageSize) {
        return apiNewsRequests
                .getNewsBySource(sources, Constants.API_KEY, pageSize);

    }

    private Observable<News> getNewsCategoryCountry(String category, String country, int pageSize) {
        return apiNewsRequests
                .getNewsCategoryCountry(category, country, Constants.API_KEY, pageSize);

    }

    private Observable<News> getNewsCategory(String category, int pageSize) {
        return apiNewsRequests
                .getNewsCategory(category, Constants.API_KEY, pageSize);

    }


}
