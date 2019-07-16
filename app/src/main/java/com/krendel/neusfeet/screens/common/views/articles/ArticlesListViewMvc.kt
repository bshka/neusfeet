package com.krendel.neusfeet.screens.common.views.articles

import androidx.databinding.ObservableField
import androidx.paging.PagedList
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewArticlesListBinding
import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.screens.common.list.ListItemActions
import com.krendel.neusfeet.screens.common.list.RecyclerPagingBindingAdapter
import com.krendel.neusfeet.screens.common.switchToChild
import com.krendel.neusfeet.screens.common.views.BaseViewMvc
import com.krendel.neusfeet.screens.common.views.CardItemsDecorator
import com.krendel.neusfeet.screens.common.views.ViewMvcActions
import com.krendel.neusfeet.screens.common.views.ViewMvcConfiguration
import io.reactivex.subjects.PublishSubject

class ArticlesListViewMvc(
    configuration: ViewMvcConfiguration
) : BaseViewMvc<ViewMvcConfiguration, ViewArticlesListBinding, ViewMvcActions>(configuration) {

    override val layout: Int = R.layout.view_articles_list
    val recyclerData = ObservableField<PagedList<ArticleItemViewModel>>()

    private val listEventsObserver = PublishSubject.create<ListItemActions>()

    override fun bindViewModel(dataBinding: ViewArticlesListBinding) {
        dataBinding.viewModel = this
    }

    init {
        dataBinding.recyclerView.addItemDecoration(CardItemsDecorator())
        val adapter = RecyclerPagingBindingAdapter(null, listEventsObserver)
        dataBinding.recyclerView.adapter = adapter
        dataBinding.refreshLayout.setOnRefreshListener {
            sendEvent(ArticlesListActions.Refresh)
        }

        registerActionsSource(
            listEventsObserver.map {
                when (it) {
                    is ArticleItemViewActions.Clicked -> ArticlesListActions.ArticleClicked(
                        it.article
                    )
                    else -> throw IllegalArgumentException("Unknown action!")
                }
            }
        )
    }

    fun showLoading(show: Boolean, isInitial: Boolean) {
        val adapter = dataBinding.recyclerView.adapter

        if (isInitial) {
            adapter?.let {
                if (show && adapter.itemCount == 0) {
                    dataBinding.viewFlipper.switchToChild(FULL_LOADING)
                } else {
                    if (adapter.itemCount == 0) {
                        dataBinding.viewFlipper.switchToChild(EMPTY_VIEW)
                    } else {
                        dataBinding.viewFlipper.switchToChild(LIST)
                    }
                }
            }
        } else {
            dataBinding.viewFlipper.switchToChild(LIST)
            dataBinding.refreshLayout.isRefreshing = show
        }
    }

    fun onError() {
        if (dataBinding.recyclerView.adapter!!.itemCount == 0) {
            dataBinding.viewFlipper.switchToChild(EMPTY_VIEW)
        }
    }

    fun setArticles(articles: PagedList<ArticleItemViewModel>) {
        dataBinding.refreshLayout.isRefreshing = false

        // TODO need to think of how to update data list. before invalidation list can be updated on it's own, after - needs to be changed with a new one
        recyclerData.set(articles)
        if (articles.isNotEmpty()) {
            showLoading(show = false, isInitial = false)
        }
    }

    companion object {
        private const val FULL_LOADING = 0
        private const val EMPTY_VIEW = 1
        private const val LIST = 2
    }
}

sealed class ArticlesListActions : ViewMvcActions {
    data class ArticleClicked(val article: Article) : ArticlesListActions()
    object Refresh : ArticlesListActions()
}