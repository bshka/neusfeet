package com.krendel.neusfeet.screens.common.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.krendel.neusfeet.common.safetySubscribe
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

abstract class BaseViewMvc<B : ViewDataBinding, A: ViewMvcActions>(
    private val lifecycleOwner: LifecycleOwner,
    inflater: LayoutInflater,
    container: ViewGroup?
) : ViewMvc, LifecycleObserver {

    /**
     * Fragment or activity should subscribe on this observable for working with ViewMvc events
     * */
    val eventsObservable: Observable<A> get() = eventSubject

    override val rootView: View
        get() = dataBinding.root

    protected val dataBinding: B = DataBindingUtil
        .inflate(inflater, layout, container, false)

    protected val context: Context
        get() = rootView.context

    /**
     * For inner rx observers
     */
    private val disposables = CompositeDisposable()

    /**
     * This subject used for notifying view about View Model events
     * */
    private val eventSubject: PublishSubject<A> = PublishSubject.create()

    init {
        @Suppress("LeakingThis")
        lifecycleOwner.lifecycle.addObserver(this)
        dataBinding.lifecycleOwner = lifecycleOwner
    }

    /**
     * Send view event to observers
     */
    protected fun sendEvent(event: A) = eventSubject.onNext(event)

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected open fun create() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected open fun start() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected open fun resume() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected open fun pause() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected open fun stop() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected open fun destroy() {
        lifecycleOwner.lifecycle.removeObserver(this)
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