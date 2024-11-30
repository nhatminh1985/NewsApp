package com.minhnn.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.minhnn.newsapp.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article ORDER BY `index` DESC")
    fun getArticlesByIndexDesc(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM article ORDER BY title DESC")
    fun getArticlesByTitleDesc(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM article ORDER BY date DESC")
    fun getArticlesByDateDesc(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Delete
    suspend fun deleteArticle(article: ArticleEntity)
}
