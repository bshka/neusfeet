package com.krendel.neusfeet.screens.preview

import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewArticlePreviewBinding
import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.screens.common.binding.Listener
import com.krendel.neusfeet.screens.common.getAttributeDimension
import com.krendel.neusfeet.screens.common.getStatusBarHeight
import com.krendel.neusfeet.screens.common.views.BaseLifecycleViewMvc
import com.krendel.neusfeet.screens.common.views.LifecycleViewMvcConfiguration
import com.krendel.neusfeet.screens.common.views.ViewMvcActions
import java.text.SimpleDateFormat
import java.util.*

class PreviewViewMvc(
    configuration: PreviewViewConfiguration
) : BaseLifecycleViewMvc<PreviewViewConfiguration, ViewArticlePreviewBinding, PreviewViewActions>(configuration) {

    override val layout: Int = R.layout.view_article_preview

    override val rootContainer: CoordinatorLayout
        get() = super.rootContainer as CoordinatorLayout

    private val article: Article = configuration.article

    val showBookmark = configuration.showBookmark
    val imageMinHeight: Int by lazy {
        context.getAttributeDimension(R.attr.actionBarSize) + context.getStatusBarHeight()
    }

    val bookmarkClick: Listener = {
        sendEvent(PreviewViewActions.BookmarkClicked(article))
    }

    val image: String? = article.urlToImage
    val title: String? = article.title
    val date: String = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
        .format(article.publishedAt ?: Date())
    val content: Spanned? = HtmlCompat.fromHtml(article.content ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
    val source: String? = article.sourceName
    val author: String? = article.author

    val readArticle: Listener = { sendEvent(PreviewViewActions.ReadArticle(article)) }

    override fun bindViewModel(dataBinding: ViewArticlePreviewBinding) {
        dataBinding.viewModel = this
    }

    fun bookmarkAdded() {
        Snackbar.make(rootContainer, R.string.bookmark_added, Snackbar.LENGTH_SHORT).show()
    }

}

sealed class PreviewViewActions : ViewMvcActions {
    data class ReadArticle(val article: Article) : PreviewViewActions()
    data class BookmarkClicked(val article: Article) : PreviewViewActions()
}

data class PreviewViewConfiguration(
    val article: Article,
    val showBookmark: Boolean,
    override val lifecycleOwner: LifecycleOwner,
    override val inflater: LayoutInflater,
    override val container: ViewGroup?
) : LifecycleViewMvcConfiguration