package com.krendel.neusfeet.screens.home

import androidx.paging.PagedList
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.list.ArticleItemViewModel
import com.krendel.neusfeet.screens.common.repository.RepositoryFactory
import com.krendel.neusfeet.screens.common.repository.common.DataSourceActions
import com.krendel.neusfeet.screens.common.repository.common.Listing
import com.krendel.neusfeet.screens.common.viewmodel.BaseActionsViewModel
import com.krendel.neusfeet.screens.common.viewmodel.ViewModelActions
import com.krendel.neusfeet.screens.common.viewmodel.registerObserver
import io.reactivex.subjects.BehaviorSubject

class HomeFragmentViewModel(
    repositoryFactory: RepositoryFactory,
    schedulersProvider: SchedulersProvider
) : BaseActionsViewModel<HomeViewModelActions>() {

    private val articlesSubject: BehaviorSubject<HomeViewModelActions.ArticlesLoaded> = BehaviorSubject.create()
    private val repositoryListing: Listing<ArticleItemViewModel>

    init {
        val repository = repositoryFactory.topHeadlinesRepository(disposables)
        repositoryListing = repository.headlines(20)

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
                        is DataSourceActions.Loading -> HomeViewModelActions.Loading(it.active)
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

}

sealed class HomeViewModelActions : ViewModelActions {
    data class ArticlesLoaded(val articles: PagedList<ArticleItemViewModel>) : HomeViewModelActions()
    data class Error(val throwable: Throwable) : HomeViewModelActions()
    data class Loading(val show: Boolean) : HomeViewModelActions()
}