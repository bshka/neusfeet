package com.krendel.neusfeet.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.krendel.neusfeet.ui.theme.NeusFeetComposeTheme

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = Screen.fromRoute(
        backstackEntry.value?.destination?.route
    )
    Scaffold(modifier = modifier) { innerPaddings ->
        Column(modifier = Modifier.padding(innerPaddings)) {
            AppNavHost(modifier = Modifier.weight(1f, true), navController = navController)
            NeusBottomNavigation(
                allScreens = Screen.values(),
                selectedScreen = currentScreen
            ) { screen ->
                navController.navigate(screen.name)
            }
        }
    }
}

@Composable
fun AppNavHost(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.HOME.route
    ) {
        composable(Screen.HOME.route) {
            Text(text = "HOME")
        }
        composable(Screen.SEARCH.route) {
            Text(text = "SEARCH")
        }
        composable(Screen.BOOKMARKS.route) {
            Text(text = "BOOKMARKS")
        }
        composable(Screen.SETTINGS.route) {
            Text(text = "SETTINGS")
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    NeusFeetComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            MainScreen()
        }
    }
}
