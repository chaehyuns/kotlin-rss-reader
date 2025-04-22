package rss.controller

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import rss.model.BlogPosts
import rss.model.RssReader
import rss.view.InputView
import rss.view.OutputView

class RssController(
    private val urls: Set<String> = rssUrls,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) {
    private var latestPosts = BlogPosts()
    private val rssReader = RssReader(ioDispatcher)

    fun run(pollingTime: Long = 60 * 10 * 1000L) = runBlocking {
        latestPosts = rssReader.fetchAllBlogs(urls)

        val inputJob = scope.launch { handleUserInput() }
        val pollingJob = scope.launch { startPolling(pollingTime) }

        joinAll(inputJob, pollingJob)
    }

    private fun handleUserInput() {
        while (scope.isActive) {
            val keyword = InputView.inputBlogKeyword()

            if (keyword.isNullOrBlank()) {
                showAllPosts()
            } else {
                showKeywordSearch(keyword)
            }
        }
    }

    private suspend fun startPolling(pollingTime: Long = 60 * 10 * 1000L) {
        var previousPosts = latestPosts

        while (scope.isActive) {
            latestPosts = rssReader.fetchAllBlogs(urls)
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
