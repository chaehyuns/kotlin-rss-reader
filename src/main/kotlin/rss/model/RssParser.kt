package rss.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.w3c.dom.Element
import java.net.URL
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory

object RssParser {
    private val baseUrl = "https://techblog.woowahan.com/feed"
    private val urlList = listOf("https://techblog.woowahan.com/feed", "https://v2.velog.io/rss/skydoves/")

    private val factory: DocumentBuilderFactory by lazy {
        DocumentBuilderFactory.newInstance()
    }

    private suspend fun fetchPosts(url: String = baseUrl): List<BlogPost> =
        withContext(Dispatchers.IO) {
            val result = mutableListOf<BlogPost>()
            val doc = factory.newDocumentBuilder().parse(URL(url).openStream())
            val items = doc.getElementsByTagName("item")
            val formatter = DateTimeFormatter.RFC_1123_DATE_TIME

            for (i in 0 until items.length) {
                val item = items.item(i) as Element
                val title = item.getElementsByTagName("title").item(0).textContent
                val link = item.getElementsByTagName("link").item(0).textContent
                val pubDate = item.getElementsByTagName("pubDate").item(0).textContent
                val date = ZonedDateTime.parse(pubDate, formatter).toLocalDate()

                result.add(BlogPost(name = title, date = date, link = link))
            }
            result
        }

    suspend fun fetchAllBlogs(urls: List<String> = urlList): List<BlogPost> =
        coroutineScope {
            val results =
                urls.map { url ->
                    async { fetchPosts(url) }
                }

            results.flatMap { it.await() }
        }
}
