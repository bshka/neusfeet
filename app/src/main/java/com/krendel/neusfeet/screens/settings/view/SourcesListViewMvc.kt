package com.krendel.neusfeet.screens.settings.view

import androidx.databinding.ObservableField
import androidx.paging.PagedList
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewSourcesListBinding
import com.krendel.neusfeet.model.source.Source
import com.krendel.neusfeet.screens.common.list.AdapterObserver
import com.krendel.neusfeet.screens.common.list.ListItemActions
import com.krendel.neusfeet.screens.common.list.RecyclerPagingBindingAdapter
import com.krendel.neusfeet.screens.common.switchToChild
import com.krendel.neusfeet.screens.common.views.BaseViewMvc
import com.krendel.neusfeet.screens.common.views.ViewMvcActions
import com.krendel.neusfeet.screens.common.views.ViewMvcConfiguration
import com.krendel.neusfeet.screens.settings.items.SourceItemActions
import com.krendel.neusfeet.screens.settings.items.SourceItemViewModel
import io.reactivex.subjects.PublishSubject

class SourcesListViewMvc(
    configuration: ViewMvcConfiguration
) : BaseViewMvc<ViewMvcConfiguration, ViewSourcesListBinding, SourcesListActions>(configuration) {

    val recyclerData = ObservableField<PagedList<SourceItemViewModel>>()
    private val listEventsObserver = PublishSubject.create<ListItemActions>()

    override val layout: Int = R.layout.view_sources_list

    override fun bindViewModel(dataBinding: ViewSourcesListBinding) {
        dataBinding.viewModel = this
    }

    init {
        val adapter = RecyclerPagingBindingAdapter(null, listEventsObserver)
        dataBinding.recyclerView.adapter = adapter
        adapter.registerAdapterDataObserver(
            AdapterObserver(
                dataBinding.viewFlipper,
                adapter
            )
        )
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
            if (show) {
                if (adapter != null && adapter.itemCount == 0) {
                    dataBinding.viewFlipper.switchToChild(0)
                } else {
                    dataBinding.viewFlipper.switchToChild(2)
                }
            }
        } else {
            dataBinding.refreshLayout.isRefreshing = show
        }
    }

    fun onError() {
        if (dataBinding.recyclerView.adapter!!.itemCount == 0) {
            dataBinding.viewFlipper.switchToChild(1)
        }
    }

    fun setSources(sources: PagedList<SourceItemViewModel>) {
        dataBinding.refreshLayout.isRefreshing = false
        recyclerData.set(sources)
    }
}

sealed class SourcesListActions : ViewMvcActions {
    object Refresh : SourcesListActions()
    data class SourceClicked(val source: Source) : SourcesListActions()
    data class ToggleSource(val source: Source, val isSelected: Boolean) : SourcesListActions()
}