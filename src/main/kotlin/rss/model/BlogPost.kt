package rss.model

import java.time.LocalDate

data class BlogPost(
    val name: String,
    val date: LocalDate,
    val link: String,
)
