package rss.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import rss.model.BlogPosts
import rss.model.RssParser
import rss.view.InputView
import rss.view.OutputView

class RssController {
    private var latestPosts = BlogPosts()
    private val rssParser = RssParser()

    fun run() = runBlocking {
        latestPosts = rssParser.fetchAllBlogs(urls = rssUrls)
        handleUserInput()
        startPolling()
    }

    private fun CoroutineScope.handleUserInput() {
        launch(Dispatchers.IO) {
            while (isActive) {
                val keyword = InputView.inputBlogKeyword()

                if (keyword.isNullOrBlank()) {
                    showAllPosts()
                } else {
                    showKeywordSearch(keyword)
                }
            }
        }
    }

    private fun CoroutineScope.startPolling(pollingTime: Long = 60 * 10 * 1000L) =
        launch {
            var previousPosts = latestPosts

            while (isActive) {
                latestPosts = rssParser.fetchAllBlogs()
                val newPosts = latestPosts.list.minus(previousPosts.list.toSet())
                if (newPosts.isNotEmpty()) {
                    OutputView.showSearchBlog(newPosts)
                }
                previousPosts = latestPosts
                delay(pollingTime)
            }
        }

    private fun showAllPosts(limit: Int = 10) {
        val topPosts = latestPosts.takeLatest(limit)
        OutputView.showSearchBlog(topPosts)
    }

    private fun showKeywordSearch(keyword: String, limit: Int = 10) {
        val filtered = latestPosts.findKeywordLatest(keyword, limit)
        OutputView.showSearchBlog(filtered)
    }

    companion object {
        private val rssUrls = setOf(
            "https://techblog.woowahan.com/feed",
            "https://v2.velog.io/rss/skydoves/"
        )
    }
}
