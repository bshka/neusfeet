package com.krendel.neusfeet.screens.settings

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.krendel.neusfeet.screens.common.BaseFragment
import com.krendel.neusfeet.screens.common.views.LifecycleViewMvcConfiguration
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SettingsFragment : BaseFragment<SettingsFragmentViewModel, SettingsViewMvc>() {

    override val viewModel: SettingsFragmentViewModel by viewModel()

    override fun createView(configuration: LifecycleViewMvcConfiguration): SettingsViewMvc =
        get { parametersOf(configuration) }

    override fun subscribeToViewModel(viewModel: SettingsFragmentViewModel) {
        observe(viewModel.eventsObservable) {
            when (it) {
                is SettingsViewModelActions.Loading -> viewMvc.showLoading(it.show, it.isInitial)
                is SettingsViewModelActions.Error -> viewMvc.onError(it.throwable)
                is SettingsViewModelActions.SourcesLoaded -> viewMvc.setSources(it.sources)
            }
        }
    }

    override fun subscribeToView(viewMvc: SettingsViewMvc) {
        observe(viewMvc.eventsObservable) {
            when (it) {
                is SettingsViewActions.Refresh -> viewModel.refresh()
                is SettingsViewActions.ToggleSource -> {
                    if (it.isSelected) {
                        viewModel.addSelection(it.source)
                    } else {
                        viewModel.removeSelection(it.source)
                    }
                }
                is SettingsViewActions.SourceClicked -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.source.url))
                    startActivity(intent)
                }
                is SettingsViewActions.CategorySelected -> {
                    Toast.makeText(context, "category ${it.code}", Toast.LENGTH_SHORT).show()
                }
                is SettingsViewActions.CountrySelected -> {
                    Toast.makeText(context, "country ${it.code}", Toast.LENGTH_SHORT).show()
                }
                is SettingsViewActions.LanguageSelected -> {
                    Toast.makeText(context, "language ${it.code}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}