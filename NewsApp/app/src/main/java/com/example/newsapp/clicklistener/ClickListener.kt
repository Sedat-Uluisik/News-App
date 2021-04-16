package com.example.newsapp.clicklistener

import android.view.View
import com.example.newsapp.model.Article

interface ClickListener {
    fun newsItemClick(view: View, news: Article)
}