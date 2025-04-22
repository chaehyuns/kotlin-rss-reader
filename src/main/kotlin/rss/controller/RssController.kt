package rss.controller

import kotlinx.coroutines.runBlocking
import rss.model.BlogPosts
import rss.model.RssParser
import rss.view.InputView
import rss.view.OutputView

class RssController {
    private lateinit var blogPosts: BlogPosts
    private val rssParser = RssParser()

    fun run() {
        runBlocking {
            blogPosts = rssParser.fetchAllBlogs(rssUrls)
        }

        val keyword = InputView.inputBlogKeyword()

        if (keyword.isNullOrEmpty()) {
            findAll()
        } else {
            findKeyword(keyword)
        }
    }

    private fun findAll() {
        val top10 = blogPosts.takeLatest(limit = 10)

        OutputView.showSearchBlog(top10)
    }

    private fun findKeyword(keyword: String) {
        val filtered = blogPosts.findKeywordLatest(keyword = keyword, limit = 10)

        OutputView.showSearchBlog(filtered)
    }

    companion object {
        private val rssUrls = setOf("https://techblog.woowahan.com/feed", "https://v2.velog.io/rss/skydoves/")
    }
}
