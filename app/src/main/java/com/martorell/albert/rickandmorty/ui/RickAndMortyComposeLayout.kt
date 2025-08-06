package com.martorell.albert.rickandmorty.ui

import androidx.compose.runtime.Composable
import com.martorell.albert.rickandmorty.ui.theme.RickAndMortyTheme

/**
 * Its goal is to apply the project theme and inflate a composable class
 * @param content it is a composable class, usually a Scaffold one
 */
@Composable
fun RickAndMortyComposeLayout(content: @Composable () -> Unit) {

    RickAndMortyTheme {
        content()
    }

}