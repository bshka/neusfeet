package com.krendel.neusfeet.screens.search

import android.widget.Toast
import androidx.paging.PagedList
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewSearchBinding
import com.krendel.neusfeet.model.Article
import com.krendel.neusfeet.screens.common.getStatusBarHeight
import com.krendel.neusfeet.screens.common.views.BaseLifecycleViewMvc
import com.krendel.neusfeet.screens.common.views.BaseViewMvcConfiguration
import com.krendel.neusfeet.screens.common.views.LifecycleViewMvcConfiguration
import com.krendel.neusfeet.screens.common.views.ViewMvcActions
import com.krendel.neusfeet.screens.common.views.articles.ArticleItemViewModel
import com.krendel.neusfeet.screens.common.views.articles.ArticlesListActions
import com.krendel.neusfeet.screens.common.views.articles.ArticlesListViewMvc
import com.krendel.neusfeet.screens.common.views.search.SearchBarViewActions
import com.krendel.neusfeet.screens.common.views.search.SearchBarViewMvc

class SearchViewMvc(
    configuration: LifecycleViewMvcConfiguration
) : BaseLifecycleViewMvc<LifecycleViewMvcConfiguration, ViewSearchBinding, SearchViewActions>(configuration) {

    override val layout: Int = R.layout.view_search

    val statusBarHeight: Int by lazy {
        context.getStatusBarHeight()
    }

    private val articlesListViewMvc: ArticlesListViewMvc

    override fun bindViewModel(dataBinding: ViewSearchBinding) {
        dataBinding.viewModel = this
    }

    init {
        val innerConfiguration = BaseViewMvcConfiguration(configuration.inflater, rootContainer)
        articlesListViewMvc = ArticlesListViewMvc(innerConfiguration)
        val searchBarViewMvc = SearchBarViewMvc(innerConfiguration)
        rootContainer.addView(searchBarViewMvc.rootView)
        rootContainer.addView(articlesListViewMvc.rootView)

        registerActionsSource(
            articlesListViewMvc.eventsObservable.map {
                when (it) {
                    is ArticlesListActions.ArticleClicked -> SearchViewActions.ArticleClicked(it.article)
                    is ArticlesListActions.Refresh -> SearchViewActions.Refresh
                    else -> throw IllegalArgumentException("Unknown action!")
                }
            }
        )
        registerActionsSource(
            searchBarViewMvc.eventsObservable.map {
                when (it) {
                    is SearchBarViewActions.SearchQuery -> SearchViewActions.SearchQuery(it.query)
                    else -> throw IllegalArgumentException("Unknown action!")
                }
            }
        )
    }

    fun setArticles(articles: PagedList<ArticleItemViewModel>) {
        articlesListViewMvc.setArticles(articles)
    }

    fun errorOccurred(throwable: Throwable) {
        articlesListViewMvc.onError()
        // TODO maybe human-readable error
        Toast.makeText(context, throwable.localizedMessage, Toast.LENGTH_SHORT).show()
    }

    fun showLoading(show: Boolean, isInitial: Boolean) {
        articlesListViewMvc.showLoading(show, isInitial)
    }
}

sealed class SearchViewActions : ViewMvcActions {
    data class ArticleClicked(val article: Article) : SearchViewActions()
    object Refresh : SearchViewActions()
    data class SearchQuery(val query: String) : SearchViewActions()
}