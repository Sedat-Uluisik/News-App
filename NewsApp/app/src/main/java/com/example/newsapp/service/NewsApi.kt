package com.example.newsapp.service

import com.example.newsapp.model.News
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    //top-headlines?country=tr&apiKey=abc
    @GET("top-headlines")
    fun getNewsFromCountry(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): Call<News>

    //top-headlines?country=tr&category=business&apiKey=abc
    //categories = business entertainment general health science sports technology
    @GET("top-headlines")
    fun getNewsFromCategory(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String
    ): Observable<News>
    //Birçok Call yapıp, bunlar hafızadan silinmez ise sorun çıkabilir, buna alternatif observable kullanıldı.
    //veriyi sürekli güncel olarak almak için.

    //everything?q=togg&language=tr&sortBy=publishedAt&apiKey=abc
    @GET("everything")
    fun getNewsFromSearch(
        @Query("q") search: String,
        @Query("language") language: String,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String
    ): Single<News>  //veriyi tek seferde, bir defa alır.
}