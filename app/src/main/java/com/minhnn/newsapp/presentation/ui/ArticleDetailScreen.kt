package com.minhnn.newsapp.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minhnn.newsapp.R
import com.minhnn.newsapp.domain.model.Article
import com.minhnn.newsapp.presentation.ui.theme.backgroundHeader

@Composable
fun DetailHeader(onBackClick: () -> Unit, onDeleteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundHeader)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BackButton(onBackClick)

        Text(
            text = stringResource(id = R.string.article_details),
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            color = Color.White,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = stringResource(id = R.string.delete),
                tint = Color.White)
        }
    }
}

@Composable
fun ArticleDetailScreen(
    article: Article,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column {
        DetailHeader(onBackClick = onBackClick, onDeleteClick = onDeleteClick)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = article.title, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = article.date, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = article.description, style = TextStyle(fontSize = 16.sp))
        }
    }
}
