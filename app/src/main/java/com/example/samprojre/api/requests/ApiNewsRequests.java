package com.example.samprojre.api.requests;

import com.example.samprojre.data.model.newsapi.News;

import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

@Singleton
public interface ApiNewsRequests {

    @GET("top-headlines")
    Observable<News> getNewsCategory(
            @Query("category") String category,
            @Query("apiKey") String apiKey,
            @Query("pageSize") int pageSize

    );
    @GET("top-headlines")
    Observable<News> getNewsCategoryCountry(
            @Query("category") String category,
            @Query("country") String country,
            @Query("apiKey") String apiKey,
            @Query("pageSize") int pageSize

    );

    @GET("top-headlines")
    Observable<News> getNewsDefault(
            @Query("country") String country,
            @Query("apiKey") String apiKey,
            @Query("pageSize") int pageSize

    );

    @GET("top-headlines")
    Observable<News> getNewsBySource(
            @Query("sources") String sources,
            @Query("apiKey") String apiKey,
            @Query("pageSize") int pageSize

    );

    @GET("everything")
    Observable<News> getNewsSearch(
            @Query("q") String keyword,
            @Query("language") String language,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey
    );

    @GET
    Observable<ResponseBody> downloadImage(@Url String url);

}
