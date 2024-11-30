package com.minhnn.newsapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article")
data class ArticleEntity(
    @PrimaryKey val index: Int,
    val title: String,
    val date: String,
    val description: String
)

