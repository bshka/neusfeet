package com.krendel.neusfeet.screens.common.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.krendel.neusfeet.common.safetySubscribe
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import timber.log.Timber

abstract class BaseViewMvc<B : ViewDataBinding, A : ViewMvcActions>(
    protected val inflater: LayoutInflater,
    container: ViewGroup?
) : ViewMvc {

    /**
     * Fragment or activity should subscribe on this observable for working with ViewMvc events
     * */
    val eventsObservable: Observable<A> get() = eventSubject

    override val rootView: View
        get() = dataBinding.root

    @Suppress("LeakingThis")
    protected val dataBinding: B by lazy {
        val binding = DataBindingUtil.inflate(inflater, layout, container, false) as B
        bindViewModel(binding)
        binding
    }

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

    /**
     * Send view event to observers
     */
    protected fun sendEvent(event: A) = eventSubject.onNext(event)

    abstract fun bindViewModel(dataBinding: B)

    @CallSuper
    protected open fun create() = Unit

    @CallSuper
    protected open fun destroy() {
        disposables.dispose()
    }

    /**
     * Register some outside source of [ViewMvcActions] in [ViewMvc] lifecycle
     */
    protected fun registerActionsSource(observable: Observable<out A>) {
        observable.registerObserver(eventSubject)
            .connectToLifecycle()
    }

    protected fun <T> observe(what: Observable<T>, action: (T) -> Unit) {
        what.safetySubscribe(
            Consumer { result: T -> action(result) },
            Consumer { error -> Timber.e(error) }
        ).connectToLifecycle()
    }

    protected fun Disposable.connectToLifecycle() = disposables.add(this)

}

fun <T : ViewMvcActions> Observable<out T>.registerObserver(eventSubject: Subject<in T>): Disposable {
    return subscribe(
        { eventSubject.onNext(it) },
        { eventSubject.onError(it) },
        { eventSubject.onComplete() },
        { eventSubject.onSubscribe(it) }
    )
}