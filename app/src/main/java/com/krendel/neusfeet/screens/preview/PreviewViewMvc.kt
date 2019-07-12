package com.krendel.neusfeet.screens.preview

import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LifecycleOwner
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

    val imageMinHeight: Int by lazy {
        context.getAttributeDimension(R.attr.actionBarSize) + context.getStatusBarHeight()
    }

    val image: String? = configuration.article.urlToImage
    val title: String? = configuration.article.title
    val date: String = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
        .format(configuration.article.publishedAt ?: Date())
    val content: Spanned? = HtmlCompat.fromHtml(configuration.article.content ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)

    val readArticle: Listener = { sendEvent(PreviewViewActions.ReadArticle(configuration.article)) }

    override fun bindViewModel(dataBinding: ViewArticlePreviewBinding) {
        dataBinding.viewModel = this
    }

}

sealed class PreviewViewActions : ViewMvcActions {
    data class ReadArticle(val article: Article) : PreviewViewActions()
}

data class PreviewViewConfiguration(
    val article: Article,
    override val lifecycleOwner: LifecycleOwner,
    override val inflater: LayoutInflater,
    override val container: ViewGroup?
) : LifecycleViewMvcConfiguration