package com.example.samprojre.data.repository;

import com.example.samprojre.api.requests.ApiSourceRequests;
import com.example.samprojre.data.model.newsapi.News;
import com.example.samprojre.utils.Constants;

import java.util.Objects;

import io.reactivex.rxjava3.core.Observable;

public class SourcesRepository {
    private final ApiSourceRequests apiSourceRequests;

    public SourcesRepository(ApiSourceRequests apiSourceRequests) {
        this.apiSourceRequests = apiSourceRequests;
    }


    public Observable<News> getSources(String country, String category) {

        if(country!=null && category!=null){
            country = country.split(", ")[1];
            return getSourcesByCountryAndCategory(country,category);
        }
        else{
            if(country!=null){
                country = country.split(", ")[1];
                return getSourcesByCountry(country);
            }
            else if(category!=null){
                return getSourcesByCategory(category);
            }
        }
        return getSourcesDefault();
    }

    private Observable<News> getSourcesDefault(){
        return apiSourceRequests.getSourcesDefault(Constants.API_KEY);
    }

    private Observable<News> getSourcesByCategory(String category){
        return apiSourceRequests.getSourcesByCategory(category,Constants.API_KEY);
    }
    private Observable<News> getSourcesByLanguage(String language){
        return apiSourceRequests.getSourcesByLanguage(language, Constants.API_KEY);
    }

    private Observable<News> getSourcesByCountry(String country){
        return apiSourceRequests.getSourcesByCountry(country,Constants.API_KEY);
    }

    private Observable<News> getSourcesByCountryAndLanguage(String country, String language){
        return apiSourceRequests.getSourcesByCountryAndLanguage(country,language,Constants.API_KEY);
    }

    private Observable<News> getSourcesByCountryAndCategory(String country, String category){
        return apiSourceRequests.getSourcesByCountryAndCategory(country,category,Constants.API_KEY);
    }

    private Observable<News> getSourcesByLanguageAndCategory(String language, String category){
        return apiSourceRequests.getSourcesByLanguageAndCategory(language,category, Constants.API_KEY);
    }

}
