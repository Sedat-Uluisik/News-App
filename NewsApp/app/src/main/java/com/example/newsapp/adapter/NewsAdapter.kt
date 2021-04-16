package com.example.newsapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.DetailActivity
import com.example.newsapp.R
import com.example.newsapp.clicklistener.ClickListener
import com.example.newsapp.databinding.NewsItemLayoutBinding
import com.example.newsapp.model.Article

class NewsAdapter(private val list: ArrayList<Article>): RecyclerView.Adapter<NewsAdapter.Holder>(), ClickListener {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<NewsItemLayoutBinding>(inflater, R.layout.news_item_layout, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NewsAdapter.Holder, position: Int) {
        val news = list[position]

        holder.item.news = news
        holder.item.clickListener = this
    }

    class Holder(var item: NewsItemLayoutBinding): RecyclerView.ViewHolder(item.root){

    }

    fun refreshData(newList: List<Article>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun newsItemClick(view: View, news: Article) {
        val intent = Intent(view.context, DetailActivity::class.java)
        intent.putExtra("author", news.source.name)
        intent.putExtra("image", news.urlToImage)
        intent.putExtra("content", news.content)
        view.context.startActivity(intent)
    }

}