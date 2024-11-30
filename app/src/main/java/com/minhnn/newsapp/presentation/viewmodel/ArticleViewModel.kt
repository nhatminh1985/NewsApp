package com.minhnn.newsapp.presentation.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.minhnn.newsapp.data.local.entity.ArticleEntity
import com.minhnn.newsapp.data.source.ArticleDataSource
import com.minhnn.newsapp.domain.model.Article
import com.minhnn.newsapp.domain.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    application: Application,
    private val articleRepository: ArticleRepository,
    private val articleDataSource: ArticleDataSource
) : AndroidViewModel(application) {

    private val _articles = MutableStateFlow<List<Article>>(emptyList())

    // Save status of sort criteria
    private val _sortCriteria = MutableStateFlow(SortCriteria.DATE) // Default sort by date
    val sortCriteria: StateFlow<SortCriteria> = _sortCriteria

    // Data of article change according to sort criteria
    @OptIn(ExperimentalCoroutinesApi::class)
    val articles: StateFlow<List<Article>> = _sortCriteria.flatMapLatest { criteria ->
        when (criteria) {
            SortCriteria.INDEX -> articleRepository.getArticlesSortedByIndex()
            SortCriteria.TITLE -> articleRepository.getArticlesSortedByTitle()
            SortCriteria.DATE -> articleRepository.getArticlesSortedByDate()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSortCriteria(criteria: SortCriteria) {
        _sortCriteria.value = criteria
    }

    // Flag check when load more is end
    var isEndOfList = mutableStateOf(false)

    // Variable for manage the page
    private var currentPage = 0
    private val pageSize = 10

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val articlesFromAssets = articleDataSource.getArticlesFromAssets()
            articleRepository.insertArticles(articlesFromAssets) // Add articles from assets to Room
        }

        loadMoreArticles()
    }

    // Load more articles
    fun loadMoreArticles() {
        viewModelScope.launch {
            if (currentPage * pageSize < _articles.value.size) {
                val start = currentPage * pageSize
                val end = minOf(start + pageSize, _articles.value.size)
                _articles.value = _articles.value.subList(0, end)
                currentPage++
            } else {
                isEndOfList.value = true
            }
        }
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            val articleEntity = ArticleEntity(
                index = article.index,
                title = article.title,
                date = article.date,
                description = article.description
            )

            withContext(Dispatchers.IO) {
                articleRepository.deleteArticle(articleEntity) // Delete article from Room
            }
        }
    }
}

// Enum that manage the sort criteria
enum class SortCriteria {
    INDEX, TITLE, DATE
}
