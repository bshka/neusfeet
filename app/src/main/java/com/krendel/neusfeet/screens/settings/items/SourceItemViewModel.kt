package com.krendel.neusfeet.screens.settings.items

import androidx.databinding.Bindable
import com.krendel.neusfeet.BR
import com.krendel.neusfeet.R
import com.krendel.neusfeet.local.source.Source
import com.krendel.neusfeet.screens.common.binding.Listener
import com.krendel.neusfeet.screens.common.list.ListItemActions
import com.krendel.neusfeet.screens.common.list.ListItemViewMvc

class SourceItemViewModel(
    private var checked: Boolean,
    private val source: Source,
    val country: String
) : ListItemViewMvc<SourceItemActions>() {

    override val layout: Int = R.layout.item_source
    override val id: Long = source.hashCode().toLong()

    val name = source.name
    val description = source.description
    val language = source.language
    val category = source.category
    val onClick: Listener = {
        checked = !checked
        notifyPropertyChanged(BR.checked)
        sendEvent(SourceItemActions.ToggleSource(source, checked))
    }
    val openSourceInBrowser: Listener = {
        sendEvent(SourceItemActions.SourceClicked(source))
    }

    override fun hasTheSameContent(other: ListItemViewMvc<*>): Boolean {
        return (other as SourceItemViewModel).source == source && other.checked == checked
    }

    @Bindable
    fun getChecked(): Boolean = checked
}

sealed class SourceItemActions : ListItemActions {
    data class SourceClicked(val source: Source) : SourceItemActions()
    data class ToggleSource(val source: Source, val isChecked: Boolean) : SourceItemActions()
}