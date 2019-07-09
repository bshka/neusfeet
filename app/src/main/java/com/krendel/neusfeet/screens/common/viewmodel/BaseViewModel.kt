package com.krendel.neusfeet.screens.common.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import com.krendel.neusfeet.common.safetySubscribe
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    protected val disposables = CompositeDisposable()

    @CallSuper
    open fun start() = Unit

    open fun resume() = Unit

    open fun pause() = Unit

    @CallSuper
    open fun stop() = Unit

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    protected fun <T> observe(what: Observable<T>, action: (T) -> Unit) {
        what.safetySubscribe(
            io.reactivex.functions.Consumer { result: T -> action(result) },
            io.reactivex.functions.Consumer { error -> Timber.e(error) }
        ).connectToLifecycle()
    }

    protected fun Disposable.connectToLifecycle() = disposables.add(this)

}