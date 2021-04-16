package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        val author_ = intent.getStringExtra("author")
        val imageUrl_ = intent.getStringExtra("image")
        val content_ = intent.getStringExtra("content")

        author.text = author_
        Glide.with(this)
            .load(imageUrl_)
            .into(newsImage)
        newsContent.text = content_

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}