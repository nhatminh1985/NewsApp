package com.minhnn.newsapp.data.repository

import com.minhnn.newsapp.data.local.dao.ArticleDao
import com.minhnn.newsapp.data.local.entity.ArticleEntity
import com.minhnn.newsapp.data.mapper.toDomain
import com.minhnn.newsapp.domain.model.Article
import com.minhnn.newsapp.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ArticleRepositoryImpl(
    private val articleDao: ArticleDao
) : ArticleRepository {

    override fun getArticlesSortedByIndex(): Flow<List<Article>> =
        articleDao.getArticlesByIndexDesc().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getArticlesSortedByTitle(): Flow<List<Article>> =
        articleDao.getArticlesByTitleDesc().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getArticlesSortedByDate(): Flow<List<Article>> =
        articleDao.getArticlesByDateDesc().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun insertArticles(articles: List<ArticleEntity>) {
        articleDao.insertArticles(articles)
    }

    override suspend fun deleteArticle(article: ArticleEntity) {
        articleDao.deleteArticle(article)
    }
}