package com.krendel.neusfeet.screens.common.repository.sources

import com.krendel.neusfeet.local.source.Source
import com.krendel.neusfeet.local.source.SourceDao
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.networking.sources.toSource
import com.krendel.neusfeet.screens.common.repository.common.DataSourceActions
import com.krendel.neusfeet.screens.common.repository.common.Listing
import com.krendel.neusfeet.screens.settings.items.SourceItemViewModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class SourcesRepositoryImpl(
    private val newsApi: NewsApi,
    private val sourcesDao: SourceDao,
    private val schedulersProvider: SchedulersProvider,
    private val compositeDisposable: CompositeDisposable,
    private val configuration: SourcesFetchConfiguration
) : SourcesRepository {

    private val dataSourceActions = PublishSubject.create<DataSourceActions>()
    private val dataPublisher: PublishSubject<List<SourceItemViewModel>> = PublishSubject.create()

    private val selectedSourcesProcessor: BehaviorProcessor<List<Source>> = BehaviorProcessor.create()

    private var disposable: Disposable? = null

    override fun sources(): Listing<SourceItemViewModel> {

        selectedSourcesProcessor.onNext(emptyList())

        // register selected sources observer
        compositeDisposable.add(
            sourcesDao.all()
                .registerObserver(selectedSourcesProcessor)
        )

        loadData()

        return Listing(
            dataList = dataPublisher,
            dataSourceActions = dataSourceActions,
            refresh = { refresh() }
        )
    }

    override fun add(source: Source) {
        compositeDisposable.add(
            Completable.create {
                try {
                    sourcesDao.add(source)
                    it.onComplete()
                } catch (e: Exception) {
                    it.tryOnError(e)
                }
            }.subscribeOn(schedulersProvider.io())
                .subscribe(
                    {},
                    {
                        Timber.e(it)
                        dataSourceActions.onNext(DataSourceActions.Error(it))
                    }
                )
        )
    }

    override fun remove(source: Source) {
        compositeDisposable.add(
            Completable.create {
                try {
                    sourcesDao.remove(source)
                    it.onComplete()
                } catch (e: Exception) {
                    it.tryOnError(e)
                }
            }.subscribeOn(schedulersProvider.io())
                .subscribe(
                    {},
                    {
                        Timber.e(it)
                        dataSourceActions.onNext(DataSourceActions.Error(it))
                    }
                )
        )
    }

    private fun refresh() {
        loadData()
    }

    private fun loadData() {
        disposable?.dispose()
        disposable = mappedFlowable()
            .observeOn(schedulersProvider.main())
            .subscribe(
                {
                    // on next
                    dataSourceActions.onNext(DataSourceActions.Loading(active = false, isInitial = false))
                    dataPublisher.onNext(it)
                },
                {
                    // on error
                    dataSourceActions.onNext(DataSourceActions.Loading(active = false, isInitial = false))
                    Timber.e(it)
                    dataSourceActions.onNext(DataSourceActions.Error(it))
                },
                {
                    // on complete
                    dataSourceActions.onNext(DataSourceActions.Loading(active = false, isInitial = false))
                },
                {
                    // on subscribe
                    // request max available items
                    it.request(Long.MAX_VALUE)
                    dataSourceActions.onNext(DataSourceActions.Loading(active = true, isInitial = true))
                }
            )
        disposable?.let(compositeDisposable::add)
    }

    private fun mappedFlowable(): Flowable<List<SourceItemViewModel>> =
        Flowable.combineLatest(selectedSourcesProcessor, loadSources(),
            BiFunction<List<Source>, List<Source>, List<SourceItemViewModel>> { selected, loaded ->
                loaded.map {
                    SourceItemViewModel(selected.contains(it), it, it.country ?: "no country")
                }
            }).observeOn(schedulersProvider.computation())

    private fun loadSources(): Flowable<List<Source>> =
        newsApi.sources(
            category = configuration.category,
            language = configuration.language,
            country = configuration.country
        ).map { it.sources!!.map { schema -> schema.toSource() } }
            .toFlowable()

}