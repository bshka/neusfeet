package com.krendel.neusfeet.screens.settings

import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.ArrayRes
import androidx.appcompat.widget.ListPopupWindow
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewSettingsBinding
import com.krendel.neusfeet.local.source.Source
import com.krendel.neusfeet.screens.common.getStatusBarHeight
import com.krendel.neusfeet.screens.common.views.BaseLifecycleViewMvc
import com.krendel.neusfeet.screens.common.views.BaseViewMvcConfiguration
import com.krendel.neusfeet.screens.common.views.LifecycleViewMvcConfiguration
import com.krendel.neusfeet.screens.common.views.ViewMvcActions
import com.krendel.neusfeet.screens.settings.items.SourceItemViewModel
import com.krendel.neusfeet.screens.settings.view.SourcesListActions
import com.krendel.neusfeet.screens.settings.view.SourcesListViewMvc

class SettingsViewMvc(
    configuration: LifecycleViewMvcConfiguration
) : BaseLifecycleViewMvc<LifecycleViewMvcConfiguration, ViewSettingsBinding, SettingsViewActions>(configuration) {

    val statusBarHeight: Int by lazy {
        context.getStatusBarHeight()
    }

    val countryClick = View.OnClickListener { view ->
        showPopupSelection(view, R.array.countries) {
            sendEvent(SettingsViewActions.CountrySelected(it))
        }
    }
    val languageClick = View.OnClickListener { view ->
        showPopupSelection(view, R.array.languages) {
            sendEvent(SettingsViewActions.LanguageSelected(it))
        }
    }
    val categoryClick = View.OnClickListener { view ->
        showPopupSelection(view, R.array.categories) {
            sendEvent(SettingsViewActions.CategorySelected(it))
        }
    }

    override val layout: Int = R.layout.view_settings

    override val rootContainer: LinearLayout
        get() = super.rootContainer as LinearLayout

    override fun bindViewModel(dataBinding: ViewSettingsBinding) {
        dataBinding.viewModel = this
    }

    private val sourcesListView = SourcesListViewMvc(BaseViewMvcConfiguration(configuration.inflater, rootContainer))

    init {
        rootContainer.addView(sourcesListView.rootView)

        registerActionsSource(
            sourcesListView.eventsObservable.map {
                when (it) {
                    is SourcesListActions.SourceClicked -> SettingsViewActions.SourceClicked(it.source)
                    is SourcesListActions.ToggleSource -> SettingsViewActions.ToggleSource(it.source, it.isSelected)
                    is SourcesListActions.Refresh -> SettingsViewActions.Refresh
                    else -> throw IllegalArgumentException("Unknown action!")
                }
            }
        )
    }

    fun showLoading(show: Boolean, isInitial: Boolean) {
        sourcesListView.showLoading(show, isInitial)
    }

    fun onError(throwable: Throwable) {
        sourcesListView.onError()
        Toast.makeText(context, throwable.localizedMessage, Toast.LENGTH_SHORT).show()
    }

    fun setSources(sources: List<SourceItemViewModel>) {
        sourcesListView.setSources(sources)
    }

    private fun showPopupSelection(view: View, @ArrayRes array: Int, onClick: (code: String?) -> Unit) {
        val popup = ListPopupWindow(view.context)
        popup.anchorView = view
        popup.setDropDownGravity(Gravity.BOTTOM)
        val arr = context.resources.getStringArray(array)
        popup.setAdapter(
            ArrayAdapter<String>(
                view.context,
                android.R.layout.simple_list_item_1,
                arr
            )
        )
        popup.setOnItemClickListener { _, _, position, _ ->
            // first is 'all', so pass null to clear filter
            val code = if (position == 0) null else arr[position]
            onClick(code)
            popup.dismiss()
        }
        popup.show()
    }

}

sealed class SettingsViewActions : ViewMvcActions {
    object Refresh : SettingsViewActions()
    data class SourceClicked(val source: Source) : SettingsViewActions()
    data class ToggleSource(val source: Source, val isSelected: Boolean) : SettingsViewActions()
    data class CountrySelected(val code: String?) : SettingsViewActions()
    data class LanguageSelected(val code: String?) : SettingsViewActions()
    data class CategorySelected(val code: String?) : SettingsViewActions()
}