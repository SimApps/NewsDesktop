package ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import data.NewsApiClient
import data.model.Article
import data.model.News
import io.ktor.client.plugins.*
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    var  news by remember { mutableStateOf( News()) }
    var headerTitle by remember{ mutableStateOf("Headlines") }
    var searchedText by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    LaunchedEffect(searchedText) {
        scope.launch{
            try {
                val newsData = if(searchedText.isNotEmpty()) {
                    NewsApiClient.getSearchedNews(searchedText)
                } else {
                    NewsApiClient.getTopHeadlines()
                }
                news = newsData
            }catch (e: ClientRequestException) {
                println("Error fetching data: ${e.message}")
            }
        }
    }

    Row {
        //SidePanel
        SidePanel(onMenuSelected = {
            headerTitle = it
            searchedText = ""
            news = News()
        }, onNewsSearched = {_searchedText, _headerTitle ->
            searchedText = _searchedText
            headerTitle = _headerTitle
            news = News()
        })
        //MainContent
        MainContent(headerTitle, news)
    }

}