package com.krendel.neusfeet.screens.bookmarks

import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewBookmarksBinding
import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.screens.common.getStatusBarHeight
import com.krendel.neusfeet.screens.common.views.BaseLifecycleViewMvc
import com.krendel.neusfeet.screens.common.views.BaseViewMvcConfiguration
import com.krendel.neusfeet.screens.common.views.LifecycleViewMvcConfiguration
import com.krendel.neusfeet.screens.common.views.ViewMvcActions
import com.krendel.neusfeet.screens.common.views.articles.ArticlesListActions
import com.krendel.neusfeet.screens.common.views.articles.ArticlesListViewMvc

class BookmarksViewMvc(
    configuration: LifecycleViewMvcConfiguration
) : BaseLifecycleViewMvc<LifecycleViewMvcConfiguration, ViewBookmarksBinding, BookmarksViewActions>(configuration) {

    override val layout: Int = R.layout.view_bookmarks

    override val rootContainer: CoordinatorLayout
        get() = super.rootContainer as CoordinatorLayout

    val statusBarHeight: Int by lazy {
        context.getStatusBarHeight()
    }

    override fun bindViewModel(dataBinding: ViewBookmarksBinding) {
        dataBinding.viewModel = this
    }

    private val articlesListViewMvc: ArticlesListViewMvc

    init {
        articlesListViewMvc = ArticlesListViewMvc(BaseViewMvcConfiguration(configuration.inflater, rootContainer))
        rootContainer.addView(articlesListViewMvc.rootView)
        articlesListViewMvc.setSwipeToRefreshEnabled(false)
        articlesListViewMvc.setSwipeToDismissEnabled(true)

        registerActionsSource(
            articlesListViewMvc.eventsObservable.map {
                when (it) {
                    is ArticlesListActions.ArticleClicked -> BookmarksViewActions.ArticleClicked(it.article)
                    is ArticlesListActions.ArticleDeleted -> BookmarksViewActions.RemoveBookmark(it.article)
                    else -> throw IllegalArgumentException("Unknown action!")
                }
            }
        )
    }
}

sealed class BookmarksViewActions : ViewMvcActions {
    data class RemoveBookmark(val article: Article) : BookmarksViewActions()
    data class ArticleClicked(val article: Article) : BookmarksViewActions()
}