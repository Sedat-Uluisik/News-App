package com.example.newsapp

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.util.CustomSharedPref
import com.example.newsapp.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    private val apikey = "abc"

    private lateinit var viewModel: NewsViewModel
    private lateinit var dataBinding: ActivityMainBinding
    private lateinit var sharedPref: CustomSharedPref
    private var adapter = NewsAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        dataBinding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)

        sharedPref = CustomSharedPref()

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)
        viewModel.getNews(apikey)

        recyclerViewNews.adapter = adapter

        observeLiveData()

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getNews(apikey)
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun observeLiveData(){
        viewModel.newsList.observe(this, Observer { news->
            news?.let {
                adapter.refreshData(it.articles)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        //Arama için işlemler.
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.search)
        val searchview = searchItem?.actionView as SearchView

        searchview.setSearchableInfo(manager.getSearchableInfo(componentName))
        searchview.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchview.clearFocus()
                searchview.setQuery("", false)
                searchItem.collapseActionView()
                viewModel.searchNews(query.toString(), apikey)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //Kategori seçenekleri.
            R.id.topHeadlines->{
                viewModel.getNews(apikey)
                true
            }
            R.id.bussiness->{
                viewModel.getNewsFromCategory("business", apikey)
                true
            }
            R.id.entertainment->{
                viewModel.getNewsFromCategory("entertainment", apikey)
                true
            }
            R.id.health->{
                viewModel.getNewsFromCategory("health", apikey)
                true
            }
            R.id.science->{
                viewModel.getNewsFromCategory("science", apikey)
                true
            }
            R.id.sports->{
                viewModel.getNewsFromCategory("sports", apikey)
                true
            }
            R.id.technology->{
                viewModel.getNewsFromCategory("technology", apikey)
                true
            }
            //Ülke seçenekleri.
            R.id.us->{
                sharedPref.saveCountry("us")
                viewModel.getNews(apikey)
                true
            }
            R.id.tr->{
                sharedPref.saveCountry("tr")
                viewModel.getNews(apikey)
                true
            }
            R.id.de->{
                sharedPref.saveCountry("de")
                viewModel.getNews(apikey)
                true
            }
            R.id.gb->{
                sharedPref.saveCountry("gb")
                viewModel.getNews(apikey)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}