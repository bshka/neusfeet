package com.krendel.neusfeet.ui.screen.bookmarks

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.krendel.neusfeet.ui.theme.NeusFeetComposeTheme

@Composable
fun BookmarksScreen() {
    Text(text = "BOOKMARKS")
}

@Preview
@Composable
fun HomeScreenPreview() {
    NeusFeetComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            BookmarksScreen()
        }
    }
}
