package com.krendel.neusfeet.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.krendel.neusfeet.ui.theme.NeusFeetComposeTheme

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier) { innerPaddings ->
        Column(modifier = Modifier.padding(innerPaddings)) {
            Spacer(modifier = Modifier.weight(1f, true))
            NeusBottomNavigation()
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
