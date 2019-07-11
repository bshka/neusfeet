package com.krendel.neusfeet.screens.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.krendel.neusfeet.screens.common.BaseFragment
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SearchFragment : BaseFragment<SearchFragmentViewModel, SearchViewMvc>() {

    override val viewModel: SearchFragmentViewModel by viewModel()

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        lifecycleOwner: LifecycleOwner
    ): SearchViewMvc = get { parametersOf(inflater, container, lifecycleOwner) }

    override fun subscribeToViewModel(viewModel: SearchFragmentViewModel) {
        observe(viewModel.eventsObservable) {
            when (it) {
                is SearchViewModelActions.ArticlesLoaded -> viewMvc.setArticles(it.articles)
                is SearchViewModelActions.Error -> viewMvc.errorOccurred(it.throwable)
                is SearchViewModelActions.Loading -> viewMvc.showLoading(it.show)
            }
        }
    }

    override fun subscribeToView(viewMvc: SearchViewMvc) {
        observe(viewMvc.eventsObservable) {
            when (it) {
                is SearchViewActions.Refresh -> viewModel.refresh()
                is SearchViewActions.SearchQuery -> viewModel.searchQuery(it.query)
                is SearchViewActions.ArticleClicked -> {
                    findNavController().navigate(SearchFragmentDirections.actionSearchToPreview(it.article))
                }
            }
        }
    }
}