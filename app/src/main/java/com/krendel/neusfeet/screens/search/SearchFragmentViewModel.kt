package com.krendel.neusfeet.screens.search

import androidx.paging.PagedList
import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.repository.common.DataSourceActions
import com.krendel.neusfeet.screens.common.repository.common.PagedListing
import com.krendel.neusfeet.screens.common.repository.everything.EverythingFetchConfiguration
import com.krendel.neusfeet.screens.common.viewmodel.BaseActionsViewModel
import com.krendel.neusfeet.screens.common.viewmodel.ViewModelActions
import com.krendel.neusfeet.screens.common.viewmodel.registerObserver
import com.krendel.neusfeet.screens.common.views.articles.ArticleItemViewModel
import com.krendel.neusfeet.screens.search.data.SearchDataInteractor
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class SearchFragmentViewModel(
    private val searchDataInteractor: SearchDataInteractor,
    private val schedulersProvider: SchedulersProvider
) : BaseActionsViewModel<SearchViewModelActions>() {

    private val articlesSubject: BehaviorSubject<SearchViewModelActions.ArticlesLoaded> = BehaviorSubject.create()
    private val repositoryListing: PagedListing<ArticleItemViewModel>

    private val configuration = EverythingFetchConfiguration(20, "")

    init {
        repositoryListing = searchDataInteractor.everything(configuration, disposables)

        // data loading
        repositoryListing
            .dataList
            .map { SearchViewModelActions.ArticlesLoaded(it) }
            .registerObserver(articlesSubject)
            .connectToLifecycle()

        // data source events mapping
        registerActionsSource(
            repositoryListing.dataSourceActions
                .map {
                    when (it) {
                        is DataSourceActions.Loading -> SearchViewModelActions.Loading(it.active, it.isInitial)
                        is DataSourceActions.Error -> SearchViewModelActions.Error(it.throwable)
                    }
                }
                .observeOn(schedulersProvider.main())
        )
    }

    override fun start() {
        super.start()
        registerDataSource(articlesSubject)
    }

    fun searchQuery(query: String) {
        configuration.query = query
        repositoryListing.refresh()
    }

    fun refresh() {
        repositoryListing.refresh()
    }

    fun addBookmark(article: Article) {
        searchDataInteractor.addBookmark(article)
            .observeOn(schedulersProvider.main())
            .subscribe(
                { sendEvent(SearchViewModelActions.BookmarkAdded) },
                { Timber.e(it) }
            ).connectToLifecycle()
    }

}

sealed class SearchViewModelActions : ViewModelActions {
    data class ArticlesLoaded(val articles: PagedList<ArticleItemViewModel>) : SearchViewModelActions()
    data class Error(val throwable: Throwable) : SearchViewModelActions()
    data class Loading(val show: Boolean, val isInitial: Boolean) : SearchViewModelActions()
    object BookmarkAdded : SearchViewModelActions()
}