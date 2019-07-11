package com.krendel.neusfeet.screens.common.views.articles

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ViewSwitcher
import androidx.databinding.ObservableField
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewArticlesListBinding
import com.krendel.neusfeet.model.Article
import com.krendel.neusfeet.screens.common.list.ListItemActions
import com.krendel.neusfeet.screens.common.list.RecyclerPagingBindingAdapter
import com.krendel.neusfeet.screens.common.views.BaseViewMvc
import com.krendel.neusfeet.screens.common.views.ViewMvcActions
import io.reactivex.subjects.PublishSubject

class ArticlesListViewMvc(
    inflater: LayoutInflater,
    container: ViewGroup?
) : BaseViewMvc<ViewArticlesListBinding, ViewMvcActions>(inflater, container) {

    override val layout: Int = R.layout.view_articles_list
    val recyclerData = ObservableField<PagedList<ArticleItemViewModel>>()

    private val listEventsObserver = PublishSubject.create<ListItemActions>()

    override fun bindViewModel(dataBinding: ViewArticlesListBinding) {
        dataBinding.viewModel = this
    }

    init {
        dataBinding.recyclerView.addItemDecoration(ArticlesItemsDecorator())
        val adapter = RecyclerPagingBindingAdapter(null, listEventsObserver)
        dataBinding.recyclerView.adapter = adapter
        adapter.registerAdapterDataObserver(AdapterObserver(dataBinding.viewSwitcher, adapter))
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
        // TODO callback for list size?
//        dataBinding.viewSwitcher.displayedChild = 1
//        dataBinding.recyclerView.doOnPreDraw {
//            dataBinding.viewSwitcher.displayedChild = if (articles.isEmpty()) 0 else 1
//        }
        dataBinding.refreshLayout.isRefreshing = false

        // TODO need to think of how to update data list. before invalidation list can be updated on it's own, after - needs to be changed with a new one
//        if (recyclerData.get() == null) {
        recyclerData.set(articles)
//        }
    }
}

sealed class ArticlesListActions : ViewMvcActions {
    data class ArticleClicked(val article: Article) : ArticlesListActions()
    object Refresh : ArticlesListActions()
}

private class AdapterObserver(
    private val viewSwitcher: ViewSwitcher,
    private val adapter: RecyclerView.Adapter<*>
) : RecyclerView.AdapterDataObserver() {
    override fun onChanged() {
//        viewSwitcher.displayedChild = if (adapter.itemCount > 0) 0 else 1
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        viewSwitcher.displayedChild = if (adapter.itemCount == 0) 0 else 1
    }
}