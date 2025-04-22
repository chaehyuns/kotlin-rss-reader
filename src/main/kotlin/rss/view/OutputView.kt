package rss.view

import rss.model.BlogPost

object OutputView {
    fun showSearchBlog(posts: List<BlogPost>) {
        if (posts.isEmpty()) {
            println("블로그 글이 없습니다.")
            return
        }
        posts.forEachIndexed { index, blog -> showSearchBlog(index, blog) }
    }

    private fun showSearchBlog(
        index: Int,
        blogPost: BlogPost,
    ) {
        println("[${index + 1}] ${blogPost.name} (${blogPost.date}) - ${blogPost.link}")
    }
}
