package com.krendel.neusfeet.screens.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.krendel.neusfeet.screens.common.viewmodel.BaseActionsViewModel
import com.krendel.neusfeet.screens.common.viewmodel.BaseViewModel
import com.krendel.neusfeet.screens.common.views.ViewMvc
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

abstract class BaseActivity<VM : BaseActionsViewModel<*>, V : ViewMvc> : AppCompatActivity() {

    abstract val viewModel: VM
    private lateinit var viewMvc: V

    private val disposables = CompositeDisposable()

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewMvc = createView(LayoutInflater.from(this), null, this)
        setContentView(viewMvc.rootView)
    }

    protected abstract fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        lifecycleOwner: LifecycleOwner
    ): V

    override fun onStart() {
        super.onStart()
        subscribeToView(viewMvc)
        subscribeToViewModel(viewModel)
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

    protected abstract fun subscribeToView(viewMvc: V)
    protected abstract fun subscribeToViewModel(viewModel: VM)

    protected fun <T> observe(what: Observable<T>, action: (T) -> Unit) {
        what.subscribe(
            { result -> action(result) },
            { error -> Timber.e(error) }
        ).connectToLifecycle()
    }

    private fun Disposable.connectToLifecycle() = disposables.add(this)

}