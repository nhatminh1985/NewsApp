package com.minhnn.newsapp.data.source

import android.content.Context
import com.google.gson.Gson
import com.minhnn.newsapp.data.local.entity.ArticleEntity

class ArticleDataSource(private val context: Context) {

    fun getArticlesFromAssets(): List<ArticleEntity> {
        val json = context.assets.open("sample_data_list.json").bufferedReader().use { it.readText() }
        return Gson().fromJson(json, Array<ArticleEntity>::class.java).toList()
    }
}