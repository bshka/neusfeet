package com.krendel.neusfeet.screens.home

import com.krendel.neusfeet.model.Article
import com.krendel.neusfeet.screens.common.viewmodel.BaseActionsViewModel
import com.krendel.neusfeet.screens.common.viewmodel.ViewModelActions

class HomeFragmentViewModel(

): BaseActionsViewModel<HomeViewModelActions>() {

    init {

    }

}

sealed class HomeViewModelActions: ViewModelActions {
    data class ArticlesLoaded(val articles: List<Article>): HomeViewModelActions()
}