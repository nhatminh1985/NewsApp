package com.minhnn.newsapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.minhnn.newsapp.data.local.dao.ArticleDao
import com.minhnn.newsapp.data.local.entity.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}