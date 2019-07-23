package com.krendel.neusfeet.screens.common.repository.sources

import com.krendel.neusfeet.local.source.Source
import com.krendel.neusfeet.screens.common.repository.common.Listing
import com.krendel.neusfeet.screens.settings.items.SourceItemViewModel
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.FlowableProcessor

interface SourcesRepository {
    fun sources(): Listing<SourceItemViewModel>
    fun add(source: Source)
    fun remove(source: Source)
}

fun <T> Flowable<out T>.registerObserver(observer: FlowableProcessor<T>): Disposable =
    subscribe(
        { observer.onNext(it) },
        { observer.onError(it) },
        { observer.onComplete() },
        { observer.onSubscribe(it) }
    )


data class SourcesFetchConfiguration(
    var country: String? = null,
    var language: String? = null,
    var category: String? = null
)