package com.krendel.neusfeet.screens.settings.items

import com.krendel.neusfeet.R
import com.krendel.neusfeet.screens.common.binding.Listener
import com.krendel.neusfeet.screens.common.list.ListItemActions
import com.krendel.neusfeet.screens.common.list.ListItemViewMvc

class LanguageItemViewModel(
    private val languageCode: String,
    val languageName: String
) : ListItemViewMvc<LanguageItemActions>() {

    override val layout: Int = R.layout.item_language
    override val id: Long = languageCode.hashCode().toLong()

    val onClick: Listener = { sendEvent(
        LanguageItemActions.LanguageClicked(
            languageCode
        )
    ) }

}

sealed class LanguageItemActions : ListItemActions {
    data class LanguageClicked(val code: String) : LanguageItemActions()
}