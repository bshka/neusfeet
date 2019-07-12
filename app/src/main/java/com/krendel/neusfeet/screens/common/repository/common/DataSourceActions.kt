package com.krendel.neusfeet.screens.common.repository.common

sealed class DataSourceActions {
    data class Error(val throwable: Throwable) : DataSourceActions()
    data class Loading(val active: Boolean, val isInitial: Boolean) : DataSourceActions()
}