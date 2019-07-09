package com.krendel.neusfeet.screens.common.list

import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.BaseObservable
import androidx.databinding.ViewDataBinding
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

abstract class ListItemViewMvc<A : ListItemActions> : BaseObservable() {

    @get:LayoutRes
    abstract val layout: Int
    val viewType: Int get() = layout

    abstract val id: Long

    protected val disposables: CompositeDisposable = CompositeDisposable()

    private val eventSubject: PublishSubject<A> = PublishSubject.create()

    val eventsObservable: Observable<A> get() = eventSubject

    protected fun sendEvent(event: A) = eventSubject.onNext(event)

    open fun isTheSameItem(other: ListItemViewMvc<*>) = this.id == other.id

    open fun hasTheSameContent(other: ListItemViewMvc<*>) = this == other

    /**
     * @param spanCount The total span count
     * @return the span of this item, from 1..spanCount
     */
    open fun getSpanSize(spanCount: Int): Int = spanCount

    /**
     * Calls every time before a recycle item view becomes visible
     */
    @CallSuper
    open fun onBind(binding: ViewDataBinding) = Unit

    /**
     * Calls every time before a recycle item view becomes invisible
     */
    @CallSuper
    open fun onUnbind(binding: ViewDataBinding) {
        disposables.clear()
    }

    /**
     * Calls once for an recycle item view when it is used for the first time
     */
    open fun onSetup(binding: ViewDataBinding) = Unit

}

interface ListItemActions

fun <T: ListItemActions> Observable<out T>.registerObserver(eventSubject: Subject<T>): Disposable {
    return subscribe(
        { eventSubject.onNext(it) },
        { eventSubject.onError(it) },
        { eventSubject.onComplete() },
        { eventSubject.onSubscribe(it) }
    )
}
