package com.example.newsapp.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.model.Article
import com.example.newsapp.model.News
import com.example.newsapp.service.NewsApiService
import com.example.newsapp.util.CustomSharedPref
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel(application: Application): BaseViewModel(application) {
    private val disposable = CompositeDisposable() //uygulama kapandığında hafızadaki verilerin silinmesini sağlar.
    private val newsApiService = NewsApiService()
    private val sharedPref = CustomSharedPref(getApplication())

    val newsList = MutableLiveData<News>()

    fun getNews(apiKey: String){  //Call ile veriler alınıyor.

        val countryCode = sharedPref.getCountry().toString()

        val call = newsApiService.getNews(countryCode, apiKey)
        call.enqueue(object :Callback<News>{   // enqueue -> asenkron olarak verileri almak için.
            override fun onFailure(call: Call<News>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<News>, response: Response<News>) {
                if(response.isSuccessful){
                    response.body()?.let {
                        newsList.value = it
                    }
                }
            }

        })
    }

    fun getNewsFromCategory(category: String, apiKey: String){  //Observable ile veriler alınıyor.
        val countryCode = sharedPref.getCountry().toString()
        disposable.add(
            newsApiService.getNewsFromCategory(countryCode, category, apiKey)
                .subscribeOn(Schedulers.io())  //main thread'ı bloklamadan, arka planda işlemi yap.
                .observeOn(AndroidSchedulers.mainThread())  //veriyi main thread'da işle.
                .subscribe(this::handleResponse)
        )
    }

    private fun handleResponse(newsList_: News){  //gelen tepkiyi(datayı) incele.
        newsList_.let {
            newsList.value = it
        }
    }

    fun searchNews(search: String, apiKey: String){
        val countryCode = sharedPref.getCountry().toString()
        disposable.add(
            newsApiService.searchNews(search, countryCode, apiKey)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<News>(){
                    override fun onSuccess(t: News) {
                        newsList.value = t
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        )
    }

    //Uygulama kapanınca hafızadaki verileri sil.
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}