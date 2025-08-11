package com.martorell.albert.rickandmorty.ui.screens.characterslist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.martorell.albert.domain.characters.app.CharacterDomain
import com.martorell.albert.rickandmorty.R
import com.martorell.albert.rickandmorty.ui.RickAndMortyComposeLayout
import com.martorell.albert.rickandmorty.ui.navigation.shared.TopAppBarCustom
import com.martorell.albert.rickandmorty.ui.screens.shared.AlertDialogCustom
import com.martorell.albert.rickandmorty.ui.screens.shared.CircularProgressIndicatorCustom
import com.martorell.albert.rickandmorty.ui.screens.shared.ErrorScreen
import kotlinx.coroutines.launch

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
    backHandlerAction: () -> Unit,
    goToDetail: (CharacterDomain) -> Unit
) {

    val state = viewModel.state.collectAsState()
    val scrollState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(scrollState)

    RickAndMortyComposeLayout {

        Scaffold(
            topBar = {
                TopAppBarCustom(
                    scrollBehavior = scrollBehavior,
                    title = stringResource(id = R.string.app_name)
                )
            }
        ) { innerPadding ->

            // Scaffold's content
            CharactersListContent(
                state = state,
                modifier = Modifier.padding(innerPadding),
                goToDetail = goToDetail,
                backHandlerAction = { backHandlerAction() },
                tryAgainAction = viewModel::downloadCharacters,
                showAlertDialogAction = viewModel::showAlertDialog,
                hideAlertDialogAction = viewModel::hideAlertDialog
            )

        }
    }

}

@Composable
fun CharactersListContent(
    state: State<CharactersViewModel.UiState>,
    modifier: Modifier = Modifier,
    goToDetail: (CharacterDomain) -> Unit,
    backHandlerAction: () -> Unit,
    tryAgainAction: () -> Unit,
    showAlertDialogAction: () -> Unit,
    hideAlertDialogAction: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    if (state.value.showAlertDialog) {

        AlertDialogCustom(
            title = R.string.favorite_dialog_title,
            content = R.string.favorite_dialog_explanation,
            actionText = R.string.favorite_dialog_accept,
            onConfirmAction = { hideAlertDialogAction() },
            onDismissAction = {})
    }

    if (state.value.error != null) {

        ErrorScreen(
            customError = state.value.error,
            setTryAgainState = {
                coroutineScope.launch { tryAgainAction() }
            },
            onBackHandlerAction = backHandlerAction
        )

    } else {

        state.value.characters.fold({
            ErrorScreen(
                customError = it, setTryAgainState = {
                    coroutineScope.launch { tryAgainAction() }
                },
                onBackHandlerAction = backHandlerAction
            )
        }) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(count = it.count()) { index ->
                    CharacterItem(
                        character = it[index],
                        clickOnRow = { goToDetail(it[index]) },
                        onFavoriteAction = { showAlertDialogAction() }
                    )
                }
            }
        }

        if (state.value.loading)
            CircularProgressIndicatorCustom()

    }

}