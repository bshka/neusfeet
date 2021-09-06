package com.krendel.neusfeet.ui.screen

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.krendel.neusfeet.ui.theme.NeusFeetComposeTheme

@Composable
fun NeusBottomNavigation(
    modifier: Modifier = Modifier,
    allScreens: Array<Screen>,
    selectedScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    BottomNavigation(modifier = modifier) {
        allScreens.forEachIndexed { _, item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(stringResource(id = item.title)) },
                selected = selectedScreen == item,
                onClick = { onScreenSelected(item) },
                alwaysShowLabel = false,
            )
        }
    }
}

@Preview
@Composable
fun PreviewNeusBottomNavigation() {
    NeusFeetComposeTheme {
        NeusBottomNavigation(
            allScreens = Screen.values(),
            selectedScreen = Screen.HOME
        ) {}
    }
}
