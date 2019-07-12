package com.krendel.neusfeet.screens.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewHomeBinding
import com.krendel.neusfeet.model.Article
import com.krendel.neusfeet.screens.common.getStatusBarHeight
import com.krendel.neusfeet.screens.common.views.BaseLifecycleViewMvc
import com.krendel.neusfeet.screens.common.views.ViewMvcActions
import com.krendel.neusfeet.screens.common.views.articles.ArticleItemViewModel
import com.krendel.neusfeet.screens.common.views.articles.ArticlesListActions
import com.krendel.neusfeet.screens.common.views.articles.ArticlesListViewMvc

class HomeViewMvc(
    inflater: LayoutInflater,
    container: ViewGroup?,
    lifecycleOwner: LifecycleOwner
) : BaseLifecycleViewMvc<ViewHomeBinding, HomeViewActions>(inflater, container, lifecycleOwner) {

    val statusBarHeight: Int by lazy {
        context.getStatusBarHeight()
    }

    override val layout: Int = R.layout.view_home
    private val articlesListViewMvc: ArticlesListViewMvc = ArticlesListViewMvc(
        inflater,
        rootView as ViewGroup
    )

    override val rootContainer: FrameLayout
        get() = super.rootContainer as FrameLayout

    override fun bindViewModel(dataBinding: ViewHomeBinding) {
        dataBinding.viewModel = this
    }

    init {
        rootContainer.addView(articlesListViewMvc.rootView)

        registerActionsSource(
            articlesListViewMvc.eventsObservable.map {
                when (it) {
                    is ArticlesListActions.ArticleClicked -> HomeViewActions.ArticleClicked(it.article)
                    is ArticlesListActions.Refresh -> HomeViewActions.Refresh
                    else -> throw IllegalArgumentException("Unknown action!")
                }
            }
        )
    }

    fun errorOccurred(throwable: Throwable) {
        // TODO maybe human-readable error
        articlesListViewMvc.onError()
        Toast.makeText(context, throwable.localizedMessage, Toast.LENGTH_SHORT).show()
    }

    fun showLoading(isLoading: Boolean, isInitial: Boolean) {
        articlesListViewMvc.showLoading(isLoading, isInitial)
    }

    fun setArticles(articles: PagedList<ArticleItemViewModel>) {
        articlesListViewMvc.setArticles(articles)
    }
}

sealed class HomeViewActions : ViewMvcActions {
    data class ArticleClicked(val article: Article) : HomeViewActions()
    object Refresh : HomeViewActions()
}