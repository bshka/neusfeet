package com.krendel.neusfeet.screens.home

import com.krendel.neusfeet.model.Article
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.usecase.FetchTopHeadlinesUseCase
import com.krendel.neusfeet.screens.common.viewmodel.BaseActionsViewModel
import com.krendel.neusfeet.screens.common.viewmodel.ViewModelActions
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class HomeFragmentViewModel(
    fetchHeadlinesUseCase: FetchTopHeadlinesUseCase,
    schedulersProvider: SchedulersProvider
) : BaseActionsViewModel<HomeViewModelActions>() {

    private val articlesSubject: BehaviorSubject<HomeViewModelActions> = BehaviorSubject.create()

    init {
        fetchHeadlinesUseCase.fetch()
            .observeOn(schedulersProvider.main())
            .subscribe(
                { articlesSubject.onNext(HomeViewModelActions.ArticlesLoaded(it)) },
                { Timber.e(it) }
            ).connectToLifecycle()
    }

    override fun start() {
        super.start()
        registerDataSource(articlesSubject)
    }

}

sealed class HomeViewModelActions : ViewModelActions {
    data class ArticlesLoaded(val articles: List<Article>) : HomeViewModelActions()
}