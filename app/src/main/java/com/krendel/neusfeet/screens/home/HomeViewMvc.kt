package com.krendel.neusfeet.screens.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewHomeBinding
import com.krendel.neusfeet.model.Article
import com.krendel.neusfeet.screens.common.list.ArticleItemViewModel
import com.krendel.neusfeet.screens.common.list.RecyclerBindingAdapter
import com.krendel.neusfeet.screens.common.views.BaseLifecycleViewMvc
import com.krendel.neusfeet.screens.common.views.ViewMvcActions

class HomeViewMvc(
    lifecycleOwner: LifecycleOwner,
    inflater: LayoutInflater,
    container: ViewGroup?
) : BaseLifecycleViewMvc<ViewHomeBinding, HomeViewActions>(lifecycleOwner, inflater, container) {

    override val layout: Int = R.layout.view_home
    val recyclerData = ObservableField<List<ArticleItemViewModel>>(emptyList())

    override fun bindViewModel(dataBinding: ViewHomeBinding) {
        dataBinding.viewModel = this
    }

    override fun create() {
        super.create()
        dataBinding.recyclerView.addItemDecoration(HomeItemsDecorator())
        dataBinding.recyclerView.adapter = RecyclerBindingAdapter(listOf(), 1)
        dataBinding.refreshLayout.isRefreshing = true
        dataBinding.refreshLayout.setOnRefreshListener {
            sendEvent(HomeViewActions.Reload)
        }
    }

    fun setArticles(articles: List<Article>) {
        dataBinding.refreshLayout.isRefreshing = false
        recyclerData.set(getArticlesViewModels(articles))
    }

    fun lockPaging() {

    }

    private fun getArticlesViewModels(articles: List<Article>): List<ArticleItemViewModel> =
        articles.map { article ->
            ArticleItemViewModel(article) {
                sendEvent(HomeViewActions.ArticleClicked(article))
            }
        }
}

sealed class HomeViewActions : ViewMvcActions {
    data class ArticleClicked(val article: Article) : HomeViewActions()
    object Reload: HomeViewActions()
}