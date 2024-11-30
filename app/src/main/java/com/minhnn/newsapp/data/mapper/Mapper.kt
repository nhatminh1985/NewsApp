package com.minhnn.newsapp.data.mapper

import com.minhnn.newsapp.data.local.entity.ArticleEntity
import com.minhnn.newsapp.domain.model.Article

fun ArticleEntity.toDomain(): Article {
    return Article(
        index = this.index,
        title = this.title,
        date = this.date,
        description = this.description
    )
}