package com.minhnn.newsapp.domain.repository

import com.minhnn.newsapp.data.local.entity.ArticleEntity
import com.minhnn.newsapp.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    suspend fun insertArticles(articles: List<ArticleEntity>)
    suspend fun deleteArticle(article: ArticleEntity)

    fun getArticlesSortedByIndex(): Flow<List<Article>>
    fun getArticlesSortedByTitle(): Flow<List<Article>>
    fun getArticlesSortedByDate(): Flow<List<Article>>
}