package com.krendel.neusfeet.screens.home

import androidx.navigation.fragment.findNavController
import com.krendel.neusfeet.screens.common.BaseFragment
import com.krendel.neusfeet.screens.common.views.LifecycleViewMvcConfiguration
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HomeFragment : BaseFragment<HomeFragmentViewModel, HomeViewMvc>() {

    override val viewModel: HomeFragmentViewModel by viewModel()

    override fun createView(configuration: LifecycleViewMvcConfiguration): HomeViewMvc =
        get { parametersOf(configuration) }

    override fun subscribeToViewModel(viewModel: HomeFragmentViewModel) {
        observe(viewModel.eventsObservable) {
            when (it) {
                is HomeViewModelActions.ArticlesLoaded -> {
                    viewMvc.setArticles(it.articles)
                }
                is HomeViewModelActions.Error -> {
                    viewMvc.errorOccurred(it.throwable)
                }
                is HomeViewModelActions.Loading -> {
                    viewMvc.showLoading(it.show, it.isInitial)
                }
            }
        }
    }

    override fun subscribeToView(viewMvc: HomeViewMvc) {
        observe(viewMvc.eventsObservable) {
            when (it) {
                is HomeViewActions.ArticleClicked -> {
                    findNavController().navigate(HomeFragmentDirections.actionHomeToPreview(it.article))
                }
                is HomeViewActions.Refresh -> {
                    viewModel.refresh()
                }
            }
        }
    }
}