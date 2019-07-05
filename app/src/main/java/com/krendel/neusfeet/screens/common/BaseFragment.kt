package com.krendel.neusfeet.screens.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.krendel.neusfeet.common.safetySubscribe
import com.krendel.neusfeet.screens.common.viewmodel.BaseActionsViewModel
import com.krendel.neusfeet.screens.common.views.ViewMvc
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

abstract class BaseFragment<VM : BaseActionsViewModel<*>, V : ViewMvc> : Fragment() {

    abstract val viewModel: VM

    private lateinit var viewMvc: V

    private val disposables = CompositeDisposable()

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewMvc = createView(inflater, container, viewLifecycleOwner)
        return viewMvc.rootView
    }

    protected abstract fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        lifecycleOwner: LifecycleOwner
    ): V

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        subscribeToViewModel(viewModel)
        subscribeToView(viewMvc)
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

    protected abstract fun subscribeToViewModel(viewModel: VM)
    protected abstract fun subscribeToView(viewMvc: V)

    protected fun <T> observe(what: Observable<T>, action: (T) -> Unit) {
        what
            .safetySubscribe(
                io.reactivex.functions.Consumer { result: T -> action(result) },
                io.reactivex.functions.Consumer { error -> Timber.e(error) }
            )
            .connectToLifecycle()
    }

    private fun Disposable.connectToLifecycle() = disposables.add(this)

}