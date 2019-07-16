package com.krendel.neusfeet.screens.settings

import com.krendel.neusfeet.local.source.Source
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.repository.RepositoryFactory
import com.krendel.neusfeet.screens.common.repository.common.DataSourceActions
import com.krendel.neusfeet.screens.common.repository.common.Listing
import com.krendel.neusfeet.screens.common.repository.sources.SourcesFetchConfiguration
import com.krendel.neusfeet.screens.common.viewmodel.BaseActionsViewModel
import com.krendel.neusfeet.screens.common.viewmodel.ViewModelActions
import com.krendel.neusfeet.screens.common.viewmodel.registerObserver
import com.krendel.neusfeet.screens.settings.items.SourceItemViewModel
import io.reactivex.subjects.BehaviorSubject

class SettingsFragmentViewModel(
    repositoryFactory: RepositoryFactory,
    schedulersProvider: SchedulersProvider
) : BaseActionsViewModel<SettingsViewModelActions>() {

    private val sourcesPublisher = BehaviorSubject.create<SettingsViewModelActions.SourcesLoaded>()
    private val repositoryListing: Listing<SourceItemViewModel>

    private val configuration = SourcesFetchConfiguration()
    val repository = repositoryFactory.sourcesRepository(configuration, disposables)

    init {
        repositoryListing = repository.sources()

        repositoryListing.dataList
            .map { SettingsViewModelActions.SourcesLoaded(it) }
            .registerObserver(sourcesPublisher)
            .connectToLifecycle()

        registerActionsSource(
            repositoryListing.dataSourceActions
                .map {
                    when (it) {
                        is DataSourceActions.Loading -> SettingsViewModelActions.Loading(it.active, it.isInitial)
                        is DataSourceActions.Error -> SettingsViewModelActions.Error(it.throwable)
                    }
                }
                .observeOn(schedulersProvider.main())
        )
    }

    override fun start() {
        super.start()
        registerDataSource(sourcesPublisher)
    }

    fun refresh() {
        repositoryListing.refresh()
    }

    fun addSelection(source: Source) {
        repository.add(source)
    }

    fun removeSelection(source: Source) {
        repository.remove(source)
    }

    fun filterByCountry(code: String?) {
        configuration.country = code
        refresh()
    }

    fun filterByLanguage(code: String?) {
        configuration.language = code
        refresh()
    }

    fun filterByCategory(code: String?) {
        configuration.category= code
        refresh()
    }

}

sealed class SettingsViewModelActions : ViewModelActions {
    data class SourcesLoaded(val sources: List<SourceItemViewModel>) : SettingsViewModelActions()
    data class Error(val throwable: Throwable) : SettingsViewModelActions()
    data class Loading(val show: Boolean, val isInitial: Boolean) : SettingsViewModelActions()
}