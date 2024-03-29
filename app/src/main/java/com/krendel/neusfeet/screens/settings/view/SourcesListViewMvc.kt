package com.krendel.neusfeet.screens.settings.view

import androidx.databinding.ObservableField
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewSourcesListBinding
import com.krendel.neusfeet.local.source.Source
import com.krendel.neusfeet.screens.common.list.ListItemActions
import com.krendel.neusfeet.screens.common.list.RecyclerBindingAdapter
import com.krendel.neusfeet.screens.common.switchToChild
import com.krendel.neusfeet.screens.common.views.BaseViewMvc
import com.krendel.neusfeet.screens.common.views.CardItemsDecorator
import com.krendel.neusfeet.screens.common.views.ViewMvcActions
import com.krendel.neusfeet.screens.common.views.ViewMvcConfiguration
import com.krendel.neusfeet.screens.settings.items.SourceItemActions
import com.krendel.neusfeet.screens.settings.items.SourceItemViewModel
import io.reactivex.subjects.PublishSubject

class SourcesListViewMvc(
    configuration: ViewMvcConfiguration
) : BaseViewMvc<ViewMvcConfiguration, ViewSourcesListBinding, SourcesListActions>(configuration) {

    val recyclerData = ObservableField<List<SourceItemViewModel>>()
    private val listEventsObserver = PublishSubject.create<ListItemActions>()

    override val layout: Int = R.layout.view_sources_list

    override fun bindViewModel(dataBinding: ViewSourcesListBinding) {
        dataBinding.viewModel = this
    }

    init {
        val adapter = RecyclerBindingAdapter(eventsObserver = listEventsObserver)
        dataBinding.recyclerView.adapter = adapter
        dataBinding.recyclerView.addItemDecoration(CardItemsDecorator())
        dataBinding.refreshLayout.setOnRefreshListener { sendEvent(SourcesListActions.Refresh) }

        registerActionsSource(
            listEventsObserver.map {
                when (it) {
                    is SourceItemActions.SourceClicked -> SourcesListActions.SourceClicked(it.source)
                    is SourceItemActions.ToggleSource -> SourcesListActions.ToggleSource(it.source, it.isChecked)
                    else -> throw IllegalArgumentException("Unknown action!")
                }
            }
        )
    }

    fun showLoading(show: Boolean, isInitial: Boolean) {
        val adapter = dataBinding.recyclerView.adapter

        if (isInitial) {
            adapter?.let {
                if (show && adapter.itemCount == 0) {
                    dataBinding.viewFlipper.switchToChild(FULL_LOADING)
                } else {
                    if (adapter.itemCount == 0) {
                        dataBinding.viewFlipper.switchToChild(EMPTY_VIEW)
                    } else {
                        dataBinding.viewFlipper.switchToChild(LIST)
                    }
                }
            }
        } else {
            dataBinding.viewFlipper.switchToChild(LIST)
            dataBinding.refreshLayout.isRefreshing = show
        }
    }

    fun onError() {
        if (dataBinding.recyclerView.adapter!!.itemCount == 0) {
            dataBinding.viewFlipper.switchToChild(1)
        }
    }

    fun setSources(sources: List<SourceItemViewModel>) {
        dataBinding.refreshLayout.isRefreshing = false
        recyclerData.set(sources)
        if (sources.isNotEmpty()) {
            showLoading(show = false, isInitial = false)
        }
    }

    companion object {
        private const val FULL_LOADING = 0
        private const val EMPTY_VIEW = 1
        private const val LIST = 2
    }
}

sealed class SourcesListActions : ViewMvcActions {
    object Refresh : SourcesListActions()
    data class SourceClicked(val source: Source) : SourcesListActions()
    data class ToggleSource(val source: Source, val isSelected: Boolean) : SourcesListActions()
}