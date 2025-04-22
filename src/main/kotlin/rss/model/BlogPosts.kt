package rss.model

class BlogPosts {
    private val list = mutableListOf<BlogPost>()

    fun add(blogPost: BlogPost) {
        list.add(blogPost)
    }

    operator fun plusAssign(other: BlogPosts) {
        this.list.addAll(other.list)
    }


    fun takeLatest(limit: Int = 10): List<BlogPost> =
        list
            .sortedByDescending { it.date }
            .take(limit)


    fun findKeywordLatest(keyword: String, limit: Int = 10, ignoreCase: Boolean = true): List<BlogPost> =
        list
            .filter { it.name.contains(keyword, ignoreCase = ignoreCase) }
            .sortedByDescending { it.date }
            .take(10)
}