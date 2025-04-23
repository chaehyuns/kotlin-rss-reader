package rss.model

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class BlogPostsTest {
    @Test
    @DisplayName("블로그 글이 추가된다")
    fun addPost() {
        // Given
        val blogPosts = BlogPosts()
        val prevPostsSize = blogPosts.list.size

        // When
        blogPosts.add(blogPost1)

        // Then
        val currentPostsSize = blogPosts.list.size
        currentPostsSize shouldBe prevPostsSize + 1
    }

    @Test
    @DisplayName("BlogPosts 글끼리 더해진다")
    fun plusAssignPost() {
        // Given
        val blogPosts1 = makeBlogPosts(2)
        val blogPosts2 = makeBlogPosts(2)
        val prevPostsSize = blogPosts1.list.size

        // When
        blogPosts1 += blogPosts2

        // Then
        blogPosts1.list.size shouldBe prevPostsSize + blogPosts2.list.size
    }

    @Test
    @DisplayName("가장 최근의 글을 가져온다")
    fun latestPost() {
        // Given
        val lastSize = 3
        val keyword = "chaehyun"
        val blogPosts = makeBlogPosts(size = lastSize, keyword = keyword)

        // When
        val result = blogPosts.takeLatest(limit = 1)

        // Then
        result.first().name shouldBe "$keyword$lastSize"
    }

    @Test
    @DisplayName("가장 최근의 글들을 limit만큼 가져온다")
    fun latestPosts() {
        // Given
        val limit = 10
        val blogPosts = makeBlogPosts(11)

        // When
        val result = blogPosts.takeLatest(limit = limit)

        // Then
        result.size shouldBe limit
    }

    @Test
    @DisplayName("BlogPost의 name에 keyword가 포함한 글들을 가져온다")
    fun findKeyword()  {
        // Given
        val size = 5
        val keyword = "chaehyun"
        val blogPosts = makeBlogPosts(size = size, keyword = keyword)

        // When
        val result = blogPosts.findKeywordLatest("ch")

        // Then
        result.size shouldBe size
    }

    @Test
    @DisplayName("BlogPost의 name에 keyword가 포함한 글을 limit만큼 가져온다")
    fun findKeywordLimit()  {
        // Given
        val size = 5
        val limit = 3
        val blogPosts = makeBlogPosts(size)

        // When
        val result = blogPosts.findKeywordLatest(keyword = "ch", limit = limit)

        // Then
        result.size shouldBe limit
    }

    @Test
    @DisplayName("BlogPost의 name에 keyword가 포함되는 글이 없을 경우 empty를 반환한다")
    fun findKeyword0()  {
        // Given
        val size = 5
        val blogPosts = makeBlogPosts(size)

        // When
        val result = blogPosts.findKeywordLatest("뷁꿹")

        // Then
        result.size shouldBe 0
    }
}
