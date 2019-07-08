package com.krendel.neusfeet.screens.common.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

abstract class BaseLifecycleViewMvc<B : ViewDataBinding, A : ViewMvcActions>(
    private val lifecycleOwner: LifecycleOwner,
    inflater: LayoutInflater,
    container: ViewGroup?
) : BaseViewMvc<B, A>(inflater, container), LifecycleObserver {

    init {
        @Suppress("LeakingThis")
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun create() {
        super.create()
        dataBinding.lifecycleOwner = lifecycleOwner
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected open fun start() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected open fun resume() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected open fun pause() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected open fun stop() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun destroy() {
        super.destroy()
        lifecycleOwner.lifecycle.removeObserver(this)
    }

}