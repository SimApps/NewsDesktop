package data

import data.model.News
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import utils.Constants

object NewsApiClient {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }

        install(Logging) {


            logger = object : Logger {
                override fun log(message: String) {
                    vLong(tag = "ktor_logger ", message = message)
                }
            }
            level = LogLevel.ALL


        }
        install(ResponseObserver) {
            onResponse { response ->
                println("http_status:"+ "${response.status.value}")
            }
        }
    }

    suspend fun getTopHeadlines(): News {
        val url = "https://newsapi.org/v2/top-headlines?country=eg&apiKey=${Constants.API_KEY}"
        return client.get(url).body()
    }

    suspend fun getSearchedNews(searchedText: String): News {
        val url = "https://newsapi.org/v2/everything?q=$searchedText&apiKey=${Constants.API_KEY}"
        return client.get(url).body()
    }




    fun vLong(tag: String, message: String) {
        val maxLength = 4000 // Maximum length of each part

        if (message.length <= maxLength) {

            println(tag + message)
        } else {
            var startIndex = 0
            var endIndex = maxLength

            while (startIndex < message.length) {
                // Ensure endIndex does not exceed the message length
                if (endIndex > message.length) {
                    endIndex = message.length
                }

                val part = message.substring(startIndex, endIndex)
                println(tag+part)

                startIndex = endIndex
                endIndex += maxLength
            }
        }
    }

}