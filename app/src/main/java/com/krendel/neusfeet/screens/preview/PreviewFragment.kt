package com.krendel.neusfeet.screens.preview

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.navArgs
import com.krendel.neusfeet.screens.common.BaseFragment
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PreviewFragment : BaseFragment<PreviewFragmentViewModel, PreviewViewMvc>() {

    override val viewModel: PreviewFragmentViewModel by viewModel()

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        lifecycleOwner: LifecycleOwner
    ): PreviewViewMvc {
        val safeArgs: PreviewFragmentArgs by navArgs()
        return get { parametersOf(safeArgs.article, lifecycleOwner, inflater, container) }
    }

    override fun subscribeToViewModel(viewModel: PreviewFragmentViewModel) {
        // still no actions from view model
//        observe(viewModel.eventsObservable) {
//            when (it) {
//
//            }
//        }
    }

    override fun subscribeToView(viewMvc: PreviewViewMvc) {
        observe(viewMvc.eventsObservable) {
            when (it) {
                is PreviewViewActions.ReadArticle -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.article.url))
                    startActivity(intent)
                }
            }
        }
    }
}