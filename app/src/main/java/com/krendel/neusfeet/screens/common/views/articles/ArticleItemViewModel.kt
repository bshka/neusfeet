package com.krendel.neusfeet.screens.common.views.articles

import com.krendel.neusfeet.R
import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.screens.common.binding.Listener
import com.krendel.neusfeet.screens.common.list.ListItemActions
import com.krendel.neusfeet.screens.common.list.ListItemViewMvc
import java.text.SimpleDateFormat
import java.util.*

class ArticleItemViewModel(
    private val article: Article
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
    val source: String? = article.source?.name

    override fun hasTheSameContent(other: ListItemViewMvc<*>): Boolean {
        return (other as ArticleItemViewModel).article == article
    }
}

sealed class ArticleItemViewActions : ListItemActions {
    data class Clicked(val article: Article) : ArticleItemViewActions()
}