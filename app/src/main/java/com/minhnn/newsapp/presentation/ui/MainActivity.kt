package com.minhnn.newsapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.minhnn.newsapp.presentation.ui.navigation.Constants
import com.minhnn.newsapp.presentation.ui.theme.NewsAppTheme
import com.minhnn.newsapp.presentation.viewmodel.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            NewsAppTheme {
                AppNavHost()
            }
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val viewModel: ArticleViewModel = hiltViewModel()
    val articles by viewModel.articles.collectAsState()

    NavHost(navController = navController, startDestination = Constants.ARTICLE_LIST) {
        composable(Constants.ARTICLE_LIST) {
            ArticleListScreen { selectedArticle ->
                navController.navigate(Constants.DETAIL + "/${selectedArticle.index}")
            }
        }

        composable(
            route = Constants.DETAIL + "/{" + Constants.ARTICLE_ID + "}",
            arguments = listOf(navArgument(Constants.ARTICLE_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getInt(Constants.ARTICLE_ID)
            val article = articles.find { it.index == articleId }

            if (article != null) {
                ArticleDetailScreen(
                    article = article,
                    onBackClick = { navController.popBackStack() },
                    onDeleteClick = {
                        viewModel.deleteArticle(article)
                        navController.popBackStack(Constants.ARTICLE_LIST, inclusive = false)
                    }
                )
            }
        }
    }
}


