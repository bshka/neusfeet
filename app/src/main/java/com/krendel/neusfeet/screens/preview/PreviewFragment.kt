package com.krendel.neusfeet.screens.preview

import android.content.Intent
import android.net.Uri
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.krendel.neusfeet.screens.common.BaseFragment
import com.krendel.neusfeet.screens.common.views.LifecycleViewMvcConfiguration
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PreviewFragment : BaseFragment<PreviewFragmentViewModel, PreviewViewMvc>() {

    override val viewModel: PreviewFragmentViewModel by viewModel()

    override fun createView(configuration: LifecycleViewMvcConfiguration): PreviewViewMvc {
        val safeArgs: PreviewFragmentArgs by navArgs()
        return get {
            parametersOf(
                PreviewViewConfiguration(
                    article = safeArgs.article,
                    showBookmark = safeArgs.showBookmark,
                    inflater = configuration.inflater,
                    container = configuration.container,
                    lifecycleOwner = configuration.lifecycleOwner
                )
            )
        }
    }

    override fun subscribeToViewModel(viewModel: PreviewFragmentViewModel) {
        observe(viewModel.eventsObservable) {
            when (it) {
                is PreviewViewModelActions.BookmarkAdded -> viewMvc.bookmarkAdded()
            }
        }
    }

    override fun subscribeToView(viewMvc: PreviewViewMvc) {
        observe(viewMvc.eventsObservable) {
            when (it) {
                is PreviewViewActions.ReadArticle -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.article.url))
                    startActivity(intent)
                }
                is PreviewViewActions.BookmarkClicked -> viewModel.addBookmark(it.article)
                is PreviewViewActions.UpClicked -> findNavController().navigateUp()
            }
        }
    }
}