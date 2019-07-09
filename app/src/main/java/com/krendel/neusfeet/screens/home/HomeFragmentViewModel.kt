package com.krendel.neusfeet.screens.home

import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.binding.Listener
import com.krendel.neusfeet.screens.common.list.ArticleItemViewModel
import com.krendel.neusfeet.screens.common.repository.Listing
import com.krendel.neusfeet.screens.common.repository.TopHeadlinesDatasourceFactory
import com.krendel.neusfeet.screens.common.repository.TopHeadlinesRepository
import com.krendel.neusfeet.screens.common.viewmodel.BaseActionsViewModel
import com.krendel.neusfeet.screens.common.viewmodel.ViewModelActions
import com.krendel.neusfeet.screens.common.viewmodel.registerObserver
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class HomeFragmentViewModel(
    newsApi: NewsApi,
    schedulersProvider: SchedulersProvider
) : BaseActionsViewModel<HomeViewModelActions>() {

    private val listingSubject = PublishSubject.create<Listing<ArticleItemViewModel>>()
    private val articlesSubject: BehaviorSubject<HomeViewModelActions> = BehaviorSubject.create()

    private val loadingError: Listener = { sendEvent(HomeViewModelActions.LoadingError) }

    private val repository = TopHeadlinesRepository(newsApi, schedulersProvider, disposables, loadingError)

//    private val sourceFactory: DataSource.Factory<Int, ArticleItemViewModel> =
//        TopHeadlinesDatasourceFactory(
//            disposables,
//            newsApi,
//            schedulersProvider,
//            loadingError
//        ).map { ArticleItemViewModel(it) }

    init {

        disposables.add(
            repository.headlines(20)
                .dataList
                .map { data ->
                    HomeViewModelActions.ArticlesLoaded(data)
                }
                .registerObserver(articlesSubject)
        )

//        val config = PagedList.Config.Builder()
//            .setPageSize(20)
//            .setInitialLoadSizeHint(20)
//            .setEnablePlaceholders(false)
//            .build()
//        disposables.add(RxPagedListBuilder(sourceFactory, config)
//            .buildObservable()
//            .map { data ->
//                HomeViewModelActions.ArticlesLoaded(data)
//            }
//            .registerObserver(articlesSubject)
//        )
    }

    override fun start() {
        super.start()
        registerDataSource(articlesSubject)
    }

    fun reload() {

    }

}

sealed class HomeViewModelActions : ViewModelActions {
    data class ArticlesLoaded(val articles: PagedList<ArticleItemViewModel>) : HomeViewModelActions()
    object LoadingError : HomeViewModelActions()
}