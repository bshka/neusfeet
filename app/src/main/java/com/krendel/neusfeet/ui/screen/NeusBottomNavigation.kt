package com.krendel.neusfeet.ui.screen

import androidx.annotation.StringRes
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.krendel.neusfeet.R
import com.krendel.neusfeet.ui.theme.NeusFeetComposeTheme

enum class Screens(
    @StringRes val title: Int,
    val icon: ImageVector
) {
    HOME(R.string.home, Icons.Filled.Home),
    SEARCH(R.string.search, Icons.Filled.Search),
    BOOKMARKS(R.string.bookmarks, Icons.Filled.Favorite),
    SETTINGS(R.string.settings, Icons.Filled.Settings)
}

@Composable
fun NeusBottomNavigation(modifier: Modifier = Modifier) {
    var selectedItem by remember { mutableStateOf(0) }
    BottomNavigation(modifier = modifier) {
        Screens.values().forEachIndexed { index, item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(stringResource(id = item.title)) },
                selected = selectedItem == index,
                onClick = { selectedItem = index },
                alwaysShowLabel = false,
            )
        }
    }
}

@Preview
@Composable
fun PreviewNeusBottomNavigation() {
    NeusFeetComposeTheme {
        NeusBottomNavigation()
    }
}
