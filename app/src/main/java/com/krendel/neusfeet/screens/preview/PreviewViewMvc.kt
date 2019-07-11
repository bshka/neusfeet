package com.krendel.neusfeet.screens.preview

import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LifecycleOwner
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewArticlePreviewBinding
import com.krendel.neusfeet.model.Article
import com.krendel.neusfeet.screens.common.binding.Listener
import com.krendel.neusfeet.screens.common.getAttributeDimension
import com.krendel.neusfeet.screens.common.getStatusBarHeight
import com.krendel.neusfeet.screens.common.views.BaseLifecycleViewMvc
import com.krendel.neusfeet.screens.common.views.ViewMvcActions
import java.text.SimpleDateFormat
import java.util.*

class PreviewViewMvc(
    private val article: Article,
    lifecycleOwner: LifecycleOwner,
    inflater: LayoutInflater,
    container: ViewGroup?
) : BaseLifecycleViewMvc<ViewArticlePreviewBinding, PreviewViewActions>(lifecycleOwner, inflater, container) {

    override val layout: Int = R.layout.view_article_preview

    val statusBarHeight: Int by lazy {
        context.getStatusBarHeight()
    }

    val imageMinHeight: Int by lazy {
        context.getAttributeDimension(R.attr.actionBarSize) + statusBarHeight
    }

    val image: String? = article.urlToImage
    val title: String? = article.title
    val date: String = SimpleDateFormat("dd MM yyyy", Locale.getDefault()).format(article.publishedAt ?: Date())
    val content: Spanned? = HtmlCompat.fromHtml(article.content ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)

    val readArticle: Listener = { sendEvent(PreviewViewActions.ReadArticle(article)) }

    override fun bindViewModel(dataBinding: ViewArticlePreviewBinding) {
        dataBinding.viewModel = this
    }

}

sealed class PreviewViewActions : ViewMvcActions {
    data class ReadArticle(val article: Article) : PreviewViewActions()
}