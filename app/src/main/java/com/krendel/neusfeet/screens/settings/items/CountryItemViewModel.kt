package com.krendel.neusfeet.screens.settings.items

import com.krendel.neusfeet.R
import com.krendel.neusfeet.screens.common.binding.Listener
import com.krendel.neusfeet.screens.common.list.ListItemActions
import com.krendel.neusfeet.screens.common.list.ListItemViewMvc

class CountryItemViewModel(
    val countryCode: String,
    val countryName: String
) : ListItemViewMvc<CountryItemActions>() {

    override val layout: Int = R.layout.item_country
    override val id: Long = countryCode.hashCode().toLong()

    val onClick: Listener = { sendEvent(
        CountryItemActions.CountryClicked(
            countryCode
        )
    ) }

}

sealed class CountryItemActions : ListItemActions {
    data class CountryClicked(val code: String) : CountryItemActions()
}