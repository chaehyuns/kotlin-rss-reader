package rss.model

import java.time.LocalDate

val blogPost1 =
    BlogPost(
        name = "chaehyun~~",
        date = LocalDate.now(),
        link = "http://굿이에요",
    )

fun makeBlogPosts(size: Int, keyword: String = "chaehyun"): BlogPosts =
    BlogPosts().apply {
        for (i in 0 until size) {
            add(
                BlogPost(
                    name = "$keyword${i + 1}",
                    date = LocalDate.now().plusDays(i.toLong()),
                    link = "http://굿이에요",
                ),
            )
        }
    }
