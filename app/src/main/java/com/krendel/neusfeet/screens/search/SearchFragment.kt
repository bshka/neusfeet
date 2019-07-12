package com.krendel.neusfeet.screens.search

import androidx.navigation.fragment.findNavController
import com.krendel.neusfeet.screens.common.BaseFragment
import com.krendel.neusfeet.screens.common.views.LifecycleViewMvcConfiguration
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SearchFragment : BaseFragment<SearchFragmentViewModel, SearchViewMvc>() {

    override val viewModel: SearchFragmentViewModel by viewModel()

    override fun createView(configuration: LifecycleViewMvcConfiguration): SearchViewMvc =
        get { parametersOf(configuration) }

    override fun subscribeToViewModel(viewModel: SearchFragmentViewModel) {
        observe(viewModel.eventsObservable) {
            when (it) {
                is SearchViewModelActions.ArticlesLoaded -> viewMvc.setArticles(it.articles)
                is SearchViewModelActions.Error -> viewMvc.errorOccurred(it.throwable)
                is SearchViewModelActions.Loading -> viewMvc.showLoading(it.show, it.isInitial)
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