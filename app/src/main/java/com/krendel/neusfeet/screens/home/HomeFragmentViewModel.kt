package com.krendel.neusfeet.screens.home

import com.krendel.neusfeet.model.Article
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.usecase.FetchTopHeadlinesUseCase
import com.krendel.neusfeet.screens.common.viewmodel.BaseActionsViewModel
import com.krendel.neusfeet.screens.common.viewmodel.ViewModelActions
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class HomeFragmentViewModel(
    private val fetchHeadlinesUseCase: FetchTopHeadlinesUseCase,
    private val schedulersProvider: SchedulersProvider
) : BaseActionsViewModel<HomeViewModelActions>() {

    private val articlesSubject: BehaviorSubject<HomeViewModelActions> = BehaviorSubject.create()

    private var page = 1
    private var totalPages = 1

    init {
        loadData(page)
    }

    override fun start() {
        super.start()
        registerDataSource(articlesSubject)
    }

    fun reload() {
        page = 1
        loadData(page)
    }

    fun loadNextPage() {
        if (page < totalPages) {
            page += 1
            loadData(page)
        }
    }

    private fun loadData(page: Int = 1) {
        fetchHeadlinesUseCase.fetch(page)
            .observeOn(schedulersProvider.main())
            .subscribe(
                {
                    val list = mutableListOf<Article>()

                    // replace data if refresh called
                    if (page > 1) {
                        articlesSubject.value?.let { action ->
                            list.addAll((action as HomeViewModelActions.ArticlesLoaded).articles)
                        }
                    }

                    list.addAll(it.articles)
                    articlesSubject.onNext(HomeViewModelActions.ArticlesLoaded(list))

                    totalPages = it.totalPages
                    if (totalPages == page) {
                        sendEvent(HomeViewModelActions.CantLoadMore)
                    }
                },
                { Timber.e(it) }
            ).connectToLifecycle()
    }

}

sealed class HomeViewModelActions : ViewModelActions {
    data class ArticlesLoaded(val articles: List<Article>) : HomeViewModelActions()
    object CantLoadMore : HomeViewModelActions()
}