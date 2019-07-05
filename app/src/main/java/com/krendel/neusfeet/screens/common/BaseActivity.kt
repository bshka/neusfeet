package com.krendel.neusfeet.screens.common

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.krendel.neusfeet.screens.common.viewmodel.BaseViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

abstract class BaseActivity<VM : BaseViewModel, B : ViewDataBinding> : AppCompatActivity() {

    abstract val viewModel: VM

    lateinit var binding: B

    @get:LayoutRes
    abstract val layout: Int


    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()

        initView()

        binding.lifecycleOwner = this
        bindViewModel()
    }

    override fun onStart() {
        super.onStart()
        subscribeToViewModel()
        viewModel.start()
    }

    override fun onResume() {
        super.onResume()
        viewModel.resume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stop()
        disposables.clear()
    }

    abstract fun bindViewModel()

    internal open fun setupView() {
        binding = DataBindingUtil.setContentView(this, layout)
    }

    protected open fun initView() = Unit

    protected open fun subscribeToViewModel() = Unit

    protected fun <T> observe(what: Observable<T>, action: (T) -> Unit) {
        what.subscribe(
            { result -> action(result) },
            { error -> Timber.e(error) }
        ).connectToLifecycle()
    }

    private fun Disposable.connectToLifecycle() = disposables.add(this)

}