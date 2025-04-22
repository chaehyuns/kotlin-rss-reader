package rss.model

class BlogPosts {
    private val _list = mutableListOf<BlogPost>()
    val list: List<BlogPost> get() = _list

    fun add(blogPost: BlogPost) {
        _list.add(blogPost)
    }

    operator fun plusAssign(other: BlogPosts) {
        this._list.addAll(other._list)
    }

    fun takeLatest(limit: Int = 10): List<BlogPost> =
        _list
            .sortedByDescending { it.date }
            .take(limit)

    fun findKeywordLatest(
        keyword: String,
        limit: Int = 10,
        ignoreCase: Boolean = true,
    ): List<BlogPost> =
        _list
            .filter { it.name.contains(keyword, ignoreCase = ignoreCase) }
            .sortedByDescending { it.date }
            .take(limit)
}
