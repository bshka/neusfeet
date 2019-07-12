package com.krendel.neusfeet.screens.settings

import android.widget.LinearLayout
import android.widget.Toast
import androidx.paging.PagedList
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewSettingsBinding
import com.krendel.neusfeet.model.source.Source
import com.krendel.neusfeet.screens.common.views.BaseLifecycleViewMvc
import com.krendel.neusfeet.screens.common.views.BaseViewMvcConfiguration
import com.krendel.neusfeet.screens.common.views.LifecycleViewMvcConfiguration
import com.krendel.neusfeet.screens.common.views.ViewMvcActions
import com.krendel.neusfeet.screens.settings.items.SourceItemViewModel
import com.krendel.neusfeet.screens.settings.view.SourcesListActions
import com.krendel.neusfeet.screens.settings.view.SourcesListViewMvc

class SettingsViewMvc(
    configuration: LifecycleViewMvcConfiguration
) : BaseLifecycleViewMvc<LifecycleViewMvcConfiguration, ViewSettingsBinding, SettingsViewActions>(configuration) {

    override val layout: Int = R.layout.view_settings

    override val rootContainer: LinearLayout
        get() = super.rootContainer as LinearLayout

    override fun bindViewModel(dataBinding: ViewSettingsBinding) {
        dataBinding.viewModel = this
    }

    private val sourcesListView = SourcesListViewMvc(BaseViewMvcConfiguration(configuration.inflater, rootContainer))

    init {
        rootContainer.addView(sourcesListView.rootView)

        registerActionsSource(
            sourcesListView.eventsObservable.map {
                when (it) {
                    is SourcesListActions.SourceClicked -> SettingsViewActions.SourceClicked(it.source)
                    is SourcesListActions.ToggleSource -> SettingsViewActions.ToggleSource(it.source, it.isSelected)
                    is SourcesListActions.Refresh -> SettingsViewActions.Refresh
                    else -> throw IllegalArgumentException("Unknown action!")
                }
            }
        )
    }

    fun showLoading(show: Boolean, isInitial: Boolean) {
        sourcesListView.showLoading(show, isInitial)
    }

    fun onError(throwable: Throwable) {
        sourcesListView.onError()
        Toast.makeText(context, throwable.localizedMessage, Toast.LENGTH_SHORT).show()
    }

    fun setSources(sources: PagedList<SourceItemViewModel>) {
        sourcesListView.setSources(sources)
    }

}

sealed class SettingsViewActions : ViewMvcActions {
    object Refresh : SettingsViewActions()
    data class SourceClicked(val source: Source) : SettingsViewActions()
    data class ToggleSource(val source: Source, val isSelected: Boolean) : SettingsViewActions()
    data class CountrySelected(val code: String?): SettingsViewActions()
    data class LanguageSelected(val code: String?): SettingsViewActions()
    data class CategorySelected(val category: String?): SettingsViewActions()
}