package com.example.samprojre.api.requests;

import com.example.samprojre.data.model.newsapi.News;

import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

@Singleton
public interface ApiSourceRequests {

    /** the response body basically has the same field names,
     *   so it is kind of pointless to use a different response model.
     *   Therefore, "News" model will suffice. Bad example of code reusability though
      */

    @GET("sources")
    Response<News> getSourcesAll(
            @Query("category") String category,
            @Query("language") String language,
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );

    @GET("sources")
    Observable<News> getSourcesByCategory(
            @Query("category") String category,
            @Query("apiKey") String apiKey
    );
    @GET("sources")
    Observable<News> getSourcesByLanguage(
            @Query("language") String language,
            @Query("apiKey") String apiKey


    );

    @GET("sources")
    Observable<News> getSourcesByCountry(
            @Query("country") String country,
            @Query("apiKey") String apiKey


    );


    @GET("sources")
    Observable<News> getSourcesDefault(
            @Query("apiKey") String apiKey
    );


    @GET("sources")
    Observable<News> getSourcesByCountryAndLanguage(
            @Query("country") String country,
            @Query("language")String language,
            @Query("apiKey") String apiKey

    );

    @GET("sources")
    Observable<News> getSourcesByCountryAndCategory(
            @Query("country") String country,
            @Query("category") String category,
            @Query("apiKey") String apiKey


    );

    @GET("sources")
    Observable<News> getSourcesByLanguageAndCategory(
            @Query("language") String language,
            @Query("category") String category,
            @Query("apiKey") String apiKey

    );

}
