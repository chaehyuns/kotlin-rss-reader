package rss.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.w3c.dom.Element
import java.net.URL
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

object RssParser {
    private val urlList = listOf("https://techblog.woowahan.com/feed", "https://v2.velog.io/rss/skydoves/")

    private val documentBuilder: DocumentBuilder by lazy {
        DocumentBuilderFactory.newInstance().newDocumentBuilder()
    }

    private suspend fun fetchPosts(url: String): BlogPosts =
        withContext(Dispatchers.IO) {
            val blogPosts = BlogPosts()
            val document = documentBuilder.parse(URL(url).openStream())
            val items = document.getElementsByTagName("item")
            val formatter = DateTimeFormatter.RFC_1123_DATE_TIME

            for (i in 0 until items.length) {
                val item = items.item(i) as Element
                val title = item.getElementsByTagName("title").item(0).textContent
                val link = item.getElementsByTagName("link").item(0).textContent
                val pubDate = item.getElementsByTagName("pubDate").item(0).textContent
                val date = ZonedDateTime.parse(pubDate, formatter).toLocalDate()

                blogPosts.add(BlogPost(name = title, date = date, link = link))
            }
            blogPosts
        }

    suspend fun fetchAllBlogs(urls: List<String> = urlList): BlogPosts =
        coroutineScope {
            val allPosts = BlogPosts()

            val results = urls.map { url ->
                async { fetchPosts(url) }
            }

            results.map { it.await() }
                .forEach { allPosts += it }

            allPosts
        }
}
