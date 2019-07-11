package com.krendel.neusfeet.screens.search

import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewSearchBarBinding
import com.krendel.neusfeet.screens.common.views.BaseViewMvc
import com.krendel.neusfeet.screens.common.views.ViewMvcActions
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SearchBarViewMvc(
    inflater: LayoutInflater,
    container: ViewGroup?
) : BaseViewMvc<ViewSearchBarBinding, SearchBarViewActions>(inflater, container) {

    override val layout: Int = R.layout.view_search_bar

    private val searchPublisher: PublishSubject<String> = PublishSubject.create()

    override fun bindViewModel(dataBinding: ViewSearchBarBinding) {
        dataBinding.viewModel = this
    }

    init {
        searchPublisher
            .throttleWithTimeout(THROTTLE_TIMEOUT, TimeUnit.MILLISECONDS)
            .subscribe { sendEvent(SearchBarViewActions.SearchQuery(it)) }
            .connectToLifecycle()
    }

    fun searchQuery(query: Editable?) {
        searchPublisher.onNext(query?.toString() ?: "")
    }

    companion object {
        private const val THROTTLE_TIMEOUT = 500L
    }
}

sealed class SearchBarViewActions : ViewMvcActions {
    data class SearchQuery(val query: String) : SearchBarViewActions()
}