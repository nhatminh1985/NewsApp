package com.minhnn.newsapp.di

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.minhnn.newsapp.data.local.dao.ArticleDao
import com.minhnn.newsapp.data.local.database.AppDatabase
import com.minhnn.newsapp.data.repository.ArticleRepositoryImpl
import com.minhnn.newsapp.data.source.ArticleDataSource
import com.minhnn.newsapp.domain.repository.ArticleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "news.db"
        ).setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .setQueryCallback({ sqlQuery, bindArgs ->
                Log.d("RoomQuery", "SQL Query: $sqlQuery | Bind Args: $bindArgs")
            }, Executors.newSingleThreadExecutor())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideArticleDao(database: AppDatabase): ArticleDao {
        return database.articleDao()
    }

    @Provides
    @Singleton
    fun provideArticleRepository(articleDao: ArticleDao): ArticleRepository {
        return ArticleRepositoryImpl(articleDao)
    }

    @Provides
    @Singleton
    fun provideArticleDataSource(context: Context): ArticleDataSource {
        return ArticleDataSource(context)
    }
}
