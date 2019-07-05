package com.krendel.neusfeet.common

import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import timber.log.Timber


/**
 * @author Alexey Zubkovskiy on 24 May 2019
 */

operator fun CompositeDisposable.plus(disposable: Disposable) {
    add(disposable)
}

fun <T> Observable<T>.safetySubscribe(
        onSuccess: Consumer<in T>,
        onError: Consumer<in Throwable> = Consumer { error: Throwable ->
            Timber.e("RxUtils onError <T> Single<T>.safetySubscribe: $error")
        }
): Disposable =
        doOnNext(onSuccess)
                .doOnError(onError)
                .ignoreElements()
                .safetySubscribe()

fun <T> Flowable<T>.safetySubscribe(): Disposable =
        ignoreElements()
                .safetySubscribe()

fun <T> Single<T>.safetySubscribe(
        onSuccess: Consumer<in T>,
        onError: Consumer<in Throwable> = Consumer { error: Throwable ->
            Timber.e("RxUtils onError <T> Single<T>.safetySubscribe: $error")
        }
): Disposable =
        doOnSuccess(onSuccess)
                .doOnError(onError)
                .ignoreElement()
                .safetySubscribe()

fun <T> Maybe<T>.safetySubscribe(): Disposable =
        ignoreElement()
                .safetySubscribe()

fun Completable.safetySubscribe(): Disposable =
        onErrorComplete()
                .subscribe()