package com.krendel.neusfeet.screens.settings.items

import android.widget.CompoundButton
import com.krendel.neusfeet.R
import com.krendel.neusfeet.model.source.Source
import com.krendel.neusfeet.screens.common.binding.Listener
import com.krendel.neusfeet.screens.common.list.ListItemActions
import com.krendel.neusfeet.screens.common.list.ListItemViewMvc

class SourceItemViewModel(
    val isChecked: Boolean,
    source: Source,
    val country: String
) : ListItemViewMvc<SourceItemActions>() {

    override val layout: Int = R.layout.item_source
    override val id: Long = source.hashCode().toLong()

    val name = source.name
    val description = source.description
    val language = source.language
    val category = source.category
    val checkListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        sendEvent(SourceItemActions.ToggleSource(source, isChecked))
    }
    val onClick: Listener = {
        sendEvent(SourceItemActions.SourceClicked(source))
    }
}

sealed class SourceItemActions : ListItemActions {
    data class SourceClicked(val source: Source) : SourceItemActions()
    data class ToggleSource(val source: Source, val isChecked: Boolean) : SourceItemActions()
}