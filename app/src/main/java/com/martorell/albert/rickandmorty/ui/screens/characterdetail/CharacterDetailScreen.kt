package com.martorell.albert.rickandmorty.ui.screens.characterdetail

import androidx.compose.foundation.layout.padding
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.martorell.albert.rickandmorty.R
import com.martorell.albert.rickandmorty.ui.RickAndMortyComposeLayout
import com.martorell.albert.rickandmorty.ui.navigation.shared.TopAppBarCustom
import com.martorell.albert.rickandmorty.ui.screens.shared.CircularProgressIndicatorCustom
import com.martorell.albert.rickandmorty.ui.screens.shared.ErrorScreen
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

/**
 * To keep the CharacterDetailContent as stateless composable (so a composable that does not hold any state),
 * we apply the programming pattern well known as state hoisting, where we move the state to the caller of a composable.
 * The simple way to do it is by replacing the state with a parameter and use functions to represent events.
 * The parameter "state" is the current value to be displayed, and the event is a lambda function that gets triggered
 * whenever the state needs to be updated. Lambdas are a common approach to describe events on a composable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersDetailScreen(
    viewModel: CharacterDetailViewModel = hiltViewModel(),
    navigationUpAction: () -> Unit,
    backHandlerAction: () -> Unit
) {

    val state = viewModel.state.collectAsState()

    CharacterDetailContent(
        state = state,
        loadCharacterAction = viewModel::loadCharacter,
        navigationUpAction = { navigationUpAction() },
        backHandlerAction = { backHandlerAction() },
        isCharacterFavoriteAction = viewModel::isCharacterFavorite,
        onFavoriteClickedAction = viewModel::onFavoriteClicked
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailContent(
    state: State<CharacterDetailViewModel.UiState>,
    loadCharacterAction: KSuspendFunction0<Unit>,
    navigationUpAction: () -> Unit,
    backHandlerAction: () -> Unit,
    isCharacterFavoriteAction: KSuspendFunction0<Boolean>,
    onFavoriteClickedAction: KSuspendFunction0<Unit>
) {

    val scrollState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(scrollState)
    val coroutineScope = rememberCoroutineScope()

    RickAndMortyComposeLayout {

        Scaffold(
            topBar = {
                TopAppBarCustom(
                    scrollBehavior = scrollBehavior,
                    title = stringResource(id = R.string.app_name),
                    showUpNavigation = true,
                    navigationUpAction = navigationUpAction
                )
            }
        ) { innerPadding ->

            if (state.value.error) {

                ErrorScreen(
                    customError = state.value.error,
                    setTryAgainState = {
                        coroutineScope.launch {
                            loadCharacterAction()
                        }
                    },
                    onBackHandlerAction = backHandlerAction
                )
            } else {

                CharacterDetailItem(
                    modifier = Modifier.padding(innerPadding),
                    character = state.value.character,
                    isCharacterFavoriteAction = isCharacterFavoriteAction,
                    onFavoriteClickedAction = onFavoriteClickedAction,
                )

            }

            if (state.value.loading)
                CircularProgressIndicatorCustom()

        }
    }

}