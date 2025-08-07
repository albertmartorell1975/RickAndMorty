package com.martorell.albert.rickandmorty.ui.screens.characterslist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.martorell.albert.rickandmorty.ui.RickAndMortyComposeLayout
import com.martorell.albert.rickandmorty.ui.navigation.shared.TopAppBarCustom
import com.martorell.albert.rickandmorty.ui.screens.shared.CircularProgressIndicatorCustom

/**
 * To keep the CharactersListContent as stateless composable (so a composable that does not hold any state),
 * we apply the programming pattern well known as state hoisting, where we move the state to the caller of a composable.
 * The simple way to do it is by replacing the state with a parameter and use functions to represent events.
 * The parameter "state" is the current value to be displayed, and the event is a lambda function that gets triggered
 * whenever the state needs to be updated. Lambdas are a common approach to describe events on a composable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersListScreen(
    viewModel: CharactersViewModel = hiltViewModel(),
    goToDetail: () -> Unit
) {

    val state = viewModel.state.collectAsState()
    val scrollState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(scrollState)

    RickAndMortyComposeLayout {

        Scaffold(
            topBar = {
                TopAppBarCustom(scrollBehavior = scrollBehavior)
            }
        ) { innerPadding ->

            // Scaffold's content
            CharactersListContent(
                state = state,
                modifier = Modifier.padding(innerPadding),
                goToDetail = { goToDetail() }
            )

        }
    }

}

@Composable
fun CharactersListContent(
    state: State<CharactersViewModel.UiState>,
    modifier: Modifier = Modifier,
    goToDetail: () -> Unit
) {

    RickAndMortyComposeLayout {

        Scaffold() { innerPadding ->

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {

                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(count = state.value.characters.size) { index ->
                        CharacterItem(
                            character = state.value.characters[index],
                            clickOnDelete = {},
                            clickOnRow = { goToDetail() }
                        )
                    }
                }

            }

            if (state.value.loading) CircularProgressIndicatorCustom()

        }
    }
}