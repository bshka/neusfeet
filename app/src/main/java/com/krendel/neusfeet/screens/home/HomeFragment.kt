package com.krendel.neusfeet.screens.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.krendel.neusfeet.screens.common.BaseFragment
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HomeFragment : BaseFragment<HomeFragmentViewModel, HomeViewMvc>() {

    override val viewModel: HomeFragmentViewModel by viewModel()

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        lifecycleOwner: LifecycleOwner
    ): HomeViewMvc = get { parametersOf(inflater, container, lifecycleOwner) }

    override fun subscribeToViewModel(viewModel: HomeFragmentViewModel) {
        observe(viewModel.eventsObservable) {
            when (it) {
                is HomeViewModelActions.ArticlesLoaded -> viewMvc.setArticles(it.articles)
            }
        }
    }

    override fun subscribeToView(viewMvc: HomeViewMvc) {
        observe(viewMvc.eventsObservable) {
            when (it) {
                is HomeViewActions.ArticleClicked -> Toast.makeText(
                    context,
                    "${it.article.title}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}