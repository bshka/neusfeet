package com.krendel.neusfeet.screens.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewHomeBinding
import com.krendel.neusfeet.model.Article
import com.krendel.neusfeet.screens.common.list.ArticleItemViewActions
import com.krendel.neusfeet.screens.common.list.ArticleItemViewModel
import com.krendel.neusfeet.screens.common.list.ListItemActions
import com.krendel.neusfeet.screens.common.list.RecyclerPagingBindingAdapter
import com.krendel.neusfeet.screens.common.views.BaseLifecycleViewMvc
import com.krendel.neusfeet.screens.common.views.ViewMvcActions
import io.reactivex.subjects.PublishSubject

class HomeViewMvc(
    lifecycleOwner: LifecycleOwner,
    inflater: LayoutInflater,
    container: ViewGroup?
) : BaseLifecycleViewMvc<ViewHomeBinding, HomeViewActions>(lifecycleOwner, inflater, container) {

    override val layout: Int = R.layout.view_home
    val recyclerData = ObservableField<PagedList<ArticleItemViewModel>>()

    private val listEventsObserver = PublishSubject.create<ListItemActions>()

    override fun bindViewModel(dataBinding: ViewHomeBinding) {
        dataBinding.viewModel = this
    }

    override fun create() {
        super.create()
        dataBinding.recyclerView.addItemDecoration(HomeItemsDecorator())
        dataBinding.recyclerView.adapter = RecyclerPagingBindingAdapter(null, listEventsObserver)
        dataBinding.refreshLayout.setOnRefreshListener {
            sendEvent(HomeViewActions.Refresh)
        }
    }

    override fun start() {
        super.start()
        observe(listEventsObserver) {
            when (it) {
                is ArticleItemViewActions.Clicked -> {
                    sendEvent(HomeViewActions.ArticleClicked(it.article))
                }
            }
        }
    }

    fun errorOccurred(throwable: Throwable) {
        // TODO maybe human-readable error
        Toast.makeText(context, throwable.localizedMessage, Toast.LENGTH_SHORT).show()
    }

    fun showLoading(show: Boolean) {
        dataBinding.refreshLayout.isRefreshing = show
    }

    fun setArticles(articles: PagedList<ArticleItemViewModel>) {
        dataBinding.refreshLayout.isRefreshing = false
        recyclerData.set(articles)
    }
}

sealed class HomeViewActions : ViewMvcActions {
    data class ArticleClicked(val article: Article) : HomeViewActions()
    object Refresh : HomeViewActions()
}