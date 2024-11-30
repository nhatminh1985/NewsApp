package com.minhnn.newsapp.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.minhnn.newsapp.R
import com.minhnn.newsapp.domain.model.Article
import com.minhnn.newsapp.presentation.ui.theme.backgroundCard
import com.minhnn.newsapp.presentation.ui.theme.backgroundHeader
import com.minhnn.newsapp.presentation.ui.theme.backgroundSort
import com.minhnn.newsapp.presentation.ui.theme.dateColor
import com.minhnn.newsapp.presentation.ui.theme.indexColor
import com.minhnn.newsapp.presentation.ui.theme.titleColor
import com.minhnn.newsapp.presentation.viewmodel.ArticleViewModel
import com.minhnn.newsapp.presentation.viewmodel.SortCriteria

@Composable
fun Header(
    selectedCriteria: SortCriteria,
    onCriteriaSelected: (SortCriteria) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundHeader)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            color = Color.White,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.weight(1f) // Modifier left
        )

        // Menu sort
        SortDropdownMenu(selectedCriteria, onCriteriaSelected)
    }
}

@Composable
fun SortDropdownMenu(
    selectedCriteria: SortCriteria,
    onCriteriaSelected: (SortCriteria) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = backgroundSort),
            shape = RoundedCornerShape(12.dp), // Round corner
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp) // Padding in button
        ) {
            Text(
                text = "Sort by: ${selectedCriteria.name}",
                style = TextStyle(fontSize = 16.sp, color = Color.White), // Font size of button
            )
        }

        // Menu sort
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            SortCriteria.entries.forEach { criteria ->
                DropdownMenuItem(
                    text = { Text(text = criteria.name) },
                    onClick = {
                        onCriteriaSelected(criteria)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ArticleListScreen(
    viewModel: ArticleViewModel = hiltViewModel(),
    onItemClick: (Article) -> Unit
) {
    val articles by viewModel.articles.collectAsState()
    val sortCriteria by viewModel.sortCriteria.collectAsState()
    val listState = rememberLazyListState()
    val isEndOfList = viewModel.isEndOfList.value

    Column {
        Header(selectedCriteria = sortCriteria,
            onCriteriaSelected = { viewModel.setSortCriteria(it) })

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(articles) { article ->
                ArticleItem(article = article, onItemClick = onItemClick)
            }

            if (!isEndOfList) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo }
                .collect { layoutInfo ->
                    val totalItems = layoutInfo.totalItemsCount
                    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                    if (totalItems - lastVisibleItemIndex <= 2 && !isEndOfList) {
                        viewModel.loadMoreArticles()
                    }
                }
        }
    }
}

@Composable
fun ArticleItem(article: Article, onItemClick: (Article) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp) // padding between Card
            .clickable { onItemClick(article) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundCard // The background color of Card
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp), // padding inside Card
            verticalAlignment = Alignment.CenterVertically // Center Vertically
        ) {
            // Index - Left
            Text(
                text = article.index.toString(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 16.sp, // Font for index
                    fontWeight = FontWeight.Bold,
                    color = indexColor // Dark blue
                ),
                modifier = Modifier
                    .weight(0.2f) // 20% weight
                    .padding(end = 8.dp) // padding between Index and Title
            )

            // Title và Date - Right
            Column(
                modifier = Modifier
                    .weight(0.8f) // 80% weight
                    .heightIn(min = 100.dp) // Min height of card
            ) {
                // Title
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp, // Font size for title
                        fontWeight = FontWeight.SemiBold,
                        color = titleColor // Dark blue for title
                    ),
                    maxLines = 2, // Limit 2 lines
                    overflow = TextOverflow.Ellipsis, // show "..." if text is too long
                    modifier = Modifier.padding(bottom = 4.dp) // Padding between Title and Date
                )
                // Date
                Text(
                    text = article.date,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp, // Front size for date
                        fontWeight = FontWeight.Normal,
                        color = dateColor // Light blue for date
                    )
                )
            }
        }
    }
}

@Composable
fun BackButton(navigateBack: () -> Unit) {

    IconButton(onClick = navigateBack) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.back),
            tint = Color.White
        )
    }
}