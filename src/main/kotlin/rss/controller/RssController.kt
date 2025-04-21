package rss.controller

import kotlinx.coroutines.runBlocking
import rss.model.BlogPost
import rss.model.RssParser
import rss.view.InputView
import rss.view.OutputView

class RssController {
    private var blogPosts: List<BlogPost> = emptyList()

    fun run() {
        runBlocking {
            blogPosts = RssParser.fetchAllBlogs(rssUrls)
        }

        val keyword = InputView.inputBlogKeyword()

        if (keyword.isNullOrEmpty()) {
            findAll()
        } else {
            findKeyword(keyword)
        }
    }

    private fun findAll() {
        val top10 =
            blogPosts
                .sortedByDescending { it.date }
                .take(10)

        OutputView.showSearchBlog(top10)
    }

    private fun findKeyword(keyword: String) {
        val filtered =
            blogPosts
                .filter { it.name.contains(keyword, ignoreCase = true) }
                .sortedByDescending { it.date }
                .take(10)

        OutputView.showSearchBlog(filtered)
    }

    companion object {
        private const val RSS_URL = "https://techblog.woowahan.com/feed"
        private val rssUrls = listOf("https://techblog.woowahan.com/feed", "https://v2.velog.io/rss/skydoves/")
    }
}
