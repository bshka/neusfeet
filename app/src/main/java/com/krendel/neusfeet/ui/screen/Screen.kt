package com.krendel.neusfeet.ui.screen

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.krendel.neusfeet.R
import timber.log.Timber

enum class Screen(
    val route: String,
    @StringRes val title: Int,
    val icon: ImageVector
) {
    HOME("home", R.string.home, Icons.Filled.Home),
    SEARCH("search", R.string.search, Icons.Filled.Search),
    BOOKMARKS("bookmarks", R.string.bookmarks, Icons.Filled.Favorite),
    SETTINGS("settings", R.string.settings, Icons.Filled.Settings);

    companion object {
        fun fromRoute(route: String?): Screen =
            when (route?.substringBefore("/")) {
                SETTINGS.route -> SETTINGS
                SEARCH.route -> SEARCH
                BOOKMARKS.route -> BOOKMARKS
                null -> HOME
                else -> {
                    Timber.e(IllegalArgumentException("Route $route is not recognized."))
                    // open home if screen is not recognized
                    HOME
                }
            }
    }

}