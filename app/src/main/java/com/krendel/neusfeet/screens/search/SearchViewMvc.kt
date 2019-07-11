package com.krendel.neusfeet.screens.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewSearchBinding
import com.krendel.neusfeet.model.Article
import com.krendel.neusfeet.screens.common.views.BaseLifecycleViewMvc
import com.krendel.neusfeet.screens.common.views.ViewMvcActions
import com.krendel.neusfeet.screens.common.views.articles.ArticleItemViewModel
import com.krendel.neusfeet.screens.common.views.articles.ArticlesListActions
import com.krendel.neusfeet.screens.common.views.articles.ArticlesListViewMvc

class SearchViewMvc(
    lifecycleOwner: LifecycleOwner,
    inflater: LayoutInflater,
    container: ViewGroup?
) : BaseLifecycleViewMvc<ViewSearchBinding, SearchViewActions>(lifecycleOwner, inflater, container) {

    override val layout: Int = R.layout.view_search

    private val articlesListViewMvc = ArticlesListViewMvc(inflater, rootContainer)

    override fun bindViewModel(dataBinding: ViewSearchBinding) {
        dataBinding.viewModel = this
    }

    init {
        val searchBarViewMvc = SearchBarViewMvc(inflater, rootContainer)
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
                }
            }
        )
    }

    fun setArticles(articles: PagedList<ArticleItemViewModel>) {
        articlesListViewMvc.setArticles(articles)
    }

    fun errorOccurred(throwable: Throwable) {
        // TODO maybe human-readable error
        Toast.makeText(context, throwable.localizedMessage, Toast.LENGTH_SHORT).show()
    }

    fun showLoading(show: Boolean) {
        articlesListViewMvc.showLoading(show)
    }
}

sealed class SearchViewActions : ViewMvcActions {
    data class ArticleClicked(val article: Article) : SearchViewActions()
    object Refresh : SearchViewActions()
    data class SearchQuery(val query: String) : SearchViewActions()
}