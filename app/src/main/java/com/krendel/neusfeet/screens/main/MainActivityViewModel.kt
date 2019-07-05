package com.krendel.neusfeet.screens.main

import com.krendel.neusfeet.screens.common.viewmodel.BaseActionsViewModel
import com.krendel.neusfeet.screens.common.viewmodel.ViewModelActions

class MainActivityViewModel : BaseActionsViewModel<MainViewModelActions>() {
}

sealed class MainViewModelActions : ViewModelActions