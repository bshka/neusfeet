package com.krendel.neusfeet.ui.screen.home

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.krendel.neusfeet.ui.theme.NeusFeetComposeTheme

@Composable
fun HomeScreen() {
    Text(text = "HOME")
}

@Preview
@Composable
fun HomeScreenPreview() {
    NeusFeetComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            HomeScreen()
        }
    }
}