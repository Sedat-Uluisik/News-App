package com.example.newsapp.service

import com.example.newsapp.model.News
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NewsApiService {
    private val BASE_URL = "https://newsapi.org/v2/"
    //--------------------------- Call yöntemi ile veri almak için.
    private val retrofitForCall = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofitForCall.create(NewsApi::class.java)
    //----------------------------

    //---------------------------- Observe ve Single yöntemi ile veri almak için.
    private val retrofitForObservableORSingle = Retrofit.Builder().baseUrl(BASE_URL)  //Observable ile veri almak için.
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(NewsApi::class.java)
    //-----------------------------

    fun getNews(countryCode: String, apiKey: String): Call<News>{
        return service.getNewsFromCountry(countryCode, apiKey)
    }

    fun getNewsFromCategory(countryCode: String, category: String, apiKey: String): Observable<News>{
        return retrofitForObservableORSingle.getNewsFromCategory(countryCode, category, apiKey)
    }

    fun searchNews(search: String, language: String, apiKey: String): Single<News>{
        return retrofitForObservableORSingle.getNewsFromSearch(search, language, "publishedAt", apiKey)
    }
}