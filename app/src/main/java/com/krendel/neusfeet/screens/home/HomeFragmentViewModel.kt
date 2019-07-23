package com.krendel.neusfeet.screens.home

import androidx.paging.PagedList
import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.repository.common.DataSourceActions
import com.krendel.neusfeet.screens.common.repository.common.PagedListing
import com.krendel.neusfeet.screens.common.repository.topheadlines.TopHeadlinesFetchConfiguration
import com.krendel.neusfeet.screens.common.viewmodel.BaseActionsViewModel
import com.krendel.neusfeet.screens.common.viewmodel.ViewModelActions
import com.krendel.neusfeet.screens.common.viewmodel.registerObserver
import com.krendel.neusfeet.screens.common.views.articles.ArticleItemViewModel
import com.krendel.neusfeet.screens.home.data.HomeDataInteractor
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class HomeFragmentViewModel(
    private val homeDataInteractor: HomeDataInteractor,
    private val schedulersProvider: SchedulersProvider
) : BaseActionsViewModel<HomeViewModelActions>() {

    private val articlesSubject: BehaviorSubject<HomeViewModelActions.ArticlesLoaded> = BehaviorSubject.create()
    private val repositoryListing: PagedListing<ArticleItemViewModel>

    private val configuration = TopHeadlinesFetchConfiguration(20)

    init {
        repositoryListing = homeDataInteractor.headlinesListing(configuration, disposables)

        // data loading
        repositoryListing
            .dataList
            .map { HomeViewModelActions.ArticlesLoaded(it) }
            .registerObserver(articlesSubject)
            .connectToLifecycle()

        // data source events mapping
        registerActionsSource(
            repositoryListing.dataSourceActions
                .map {
                    when (it) {
                        is DataSourceActions.Loading -> HomeViewModelActions.Loading(it.active, it.isInitial)
                        is DataSourceActions.Error -> HomeViewModelActions.Error(it.throwable)
                    }
                }
                .observeOn(schedulersProvider.main())
        )
    }

    override fun start() {
        super.start()
        registerDataSource(articlesSubject)
    }

    fun refresh() {
        repositoryListing.refresh()
    }

    fun addBookmark(article: Article) {
        homeDataInteractor.addBookmark(article)
            .observeOn(schedulersProvider.main())
            .subscribe(
                { sendEvent(HomeViewModelActions.BookmarkAdded) },
                { Timber.e(it) }
            ).connectToLifecycle()
    }

}

sealed class HomeViewModelActions : ViewModelActions {
    data class ArticlesLoaded(val articles: PagedList<ArticleItemViewModel>) : HomeViewModelActions()
    object BookmarkAdded : HomeViewModelActions()
    data class Error(val throwable: Throwable) : HomeViewModelActions()
    data class Loading(val show: Boolean, val isInitial: Boolean) : HomeViewModelActions()
}