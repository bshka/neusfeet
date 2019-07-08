package com.krendel.neusfeet.screens.common.list

import com.krendel.neusfeet.R
import com.krendel.neusfeet.model.Article
import com.krendel.neusfeet.screens.common.binding.Listener
import java.text.SimpleDateFormat
import java.util.*

class ArticleItemViewModel(
    article: Article,
    val onClick: Listener
) : ListItemViewMvc() {

    override val layout: Int = R.layout.item_article
    override val id: Long = article.hashCode().toLong()

    val image: String? = article.urlToImage
    val title: String? = article.title
    val date: String = SimpleDateFormat("dd MM yyyy", Locale.getDefault()).format(article.publishedAt ?: Date())
    val description: String? = article.description

    override fun getSpanSize(spanCount: Int): Int = 1
}