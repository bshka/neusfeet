package com.krendel.neusfeet.screens.common.views.articles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewArticlesListBinding
import com.krendel.neusfeet.model.Article
import com.krendel.neusfeet.screens.common.list.ListItemActions
import com.krendel.neusfeet.screens.common.list.RecyclerPagingBindingAdapter
import com.krendel.neusfeet.screens.common.views.BaseLifecycleViewMvc
import com.krendel.neusfeet.screens.common.views.ViewMvcActions
import com.krendel.neusfeet.screens.home.HomeItemsDecorator
import io.reactivex.subjects.PublishSubject

class ArticlesListViewMvc(
    lifecycleOwner: LifecycleOwner,
    inflater: LayoutInflater,
    container: ViewGroup?
) : BaseLifecycleViewMvc<ViewArticlesListBinding, ViewMvcActions>(lifecycleOwner, inflater, container) {

    override val layout: Int = R.layout.view_articles_list
    val recyclerData = ObservableField<PagedList<ArticleItemViewModel>>()

    private val listEventsObserver = PublishSubject.create<ListItemActions>()

    override fun bindViewModel(dataBinding: ViewArticlesListBinding) {
        dataBinding.viewModel = this
    }

    override fun create() {
        super.create()
        dataBinding.recyclerView.addItemDecoration(HomeItemsDecorator())
        dataBinding.recyclerView.adapter = RecyclerPagingBindingAdapter(null, listEventsObserver)
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

    fun showLoading(show: Boolean) {
        dataBinding.refreshLayout.isRefreshing = show
    }

    fun setArticles(articles: PagedList<ArticleItemViewModel>) {
        dataBinding.refreshLayout.isRefreshing = false
        recyclerData.set(articles)
    }
}

sealed class ArticlesListActions : ViewMvcActions {
    data class ArticleClicked(val article: Article) : ArticlesListActions()
    object Refresh: ArticlesListActions()
}