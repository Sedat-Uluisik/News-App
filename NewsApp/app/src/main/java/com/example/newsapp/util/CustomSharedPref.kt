package com.example.newsapp.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class CustomSharedPref {

    //Ülke kodunu saklamak için kullanıldı.

    companion object{
        private val PREF_KEY = "news_lang"
        private var sharedPreferences: SharedPreferences ?= null

        @Volatile private var instance: CustomSharedPref ?= null  //customSharedPref classı ndan obje oluşturuldu.
        //@Volatile -> coroutine fonk ve farklı thread lerde kullanılabilieceği için kullanıldı.
        private val lock = Any()

        operator fun invoke(context: Context): CustomSharedPref = instance ?: synchronized(lock){  //instance varsa kullan yoksa oluştur.
            instance ?: createSharedPref(context).also {
                instance = it
            }
        }

        private fun createSharedPref(context: Context): CustomSharedPref{
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return CustomSharedPref()
        }
    }

    fun saveCountry(countryCode: String){
        sharedPreferences?.edit(commit = true){
            putString(PREF_KEY, countryCode)
        }
    }

    fun getCountry() = sharedPreferences?.getString(PREF_KEY, "tr")
}