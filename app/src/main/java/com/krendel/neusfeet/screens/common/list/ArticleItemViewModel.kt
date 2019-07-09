package com.krendel.neusfeet.screens.common.list

import com.krendel.neusfeet.R
import com.krendel.neusfeet.model.Article
import com.krendel.neusfeet.screens.common.binding.Listener
import java.text.SimpleDateFormat
import java.util.*

class ArticleItemViewModel(
    article: Article
) : ListItemViewMvc<ArticleItemViewActions>() {

    override val layout: Int = R.layout.item_article
    override val id: Long = article.hashCode().toLong()

    val onClick: Listener = {
        sendEvent(ArticleItemViewActions.Clicked(article))
    }

    val image: String? = article.urlToImage
    val title: String? = article.title
    val date: String = SimpleDateFormat("dd MM yyyy", Locale.getDefault()).format(article.publishedAt ?: Date())
    val description: String? = article.description

    override fun getSpanSize(spanCount: Int): Int = 1
}

sealed class ArticleItemViewActions : ListItemActions {
    data class Clicked(val article: Article) : ArticleItemViewActions()
}