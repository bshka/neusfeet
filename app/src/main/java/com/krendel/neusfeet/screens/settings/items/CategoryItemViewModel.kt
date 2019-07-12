package com.krendel.neusfeet.screens.settings.items

import com.krendel.neusfeet.R
import com.krendel.neusfeet.screens.common.binding.Listener
import com.krendel.neusfeet.screens.common.list.ListItemActions
import com.krendel.neusfeet.screens.common.list.ListItemViewMvc

class CategoryItemViewModel(
    categoryCode: String,
    val categoryName: String
) : ListItemViewMvc<CategoryItemActions>() {
    override val layout: Int = R.layout.item_category
    override val id: Long = categoryCode.hashCode().toLong()

    val onClick: Listener = { sendEvent(CategoryItemActions.CategoryClicked(categoryCode)) }
}

sealed class CategoryItemActions : ListItemActions {
    data class CategoryClicked(val code: String) : CategoryItemActions()
}