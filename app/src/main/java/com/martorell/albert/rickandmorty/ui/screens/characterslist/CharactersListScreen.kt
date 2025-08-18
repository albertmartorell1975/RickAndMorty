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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.martorell.albert.domain.characters.app.CharacterDomain
import com.martorell.albert.rickandmorty.R
import com.martorell.albert.rickandmorty.ui.RickAndMortyComposeLayout
import com.martorell.albert.rickandmorty.ui.navigation.shared.TopAppBarCustom
import com.martorell.albert.rickandmorty.ui.screens.shared.CircularProgressIndicatorCustom
import com.martorell.albert.rickandmorty.ui.screens.shared.ErrorScreen
import com.martorell.albert.rickandmorty.ui.screens.shared.SnackBarCustom
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1
import kotlin.reflect.KSuspendFunction1

/**
 * To keep the CharactersListContent as stateless composable (so a composable that does not hold any state),
 * we apply the programming pattern well known as state hoisting, where we move the state to the caller of a composable.
 * The simple way to do it is by replacing the state with a parameter and use functions to represent events.
 * The parameter "state" is the current value to be displayed, and the event is a lambda function that gets triggered
 * whenever the state needs to be updated. Lambdas are a common approach to describe events on a composable
 */

@Composable
fun CharactersListScreen(
    viewModel: CharactersViewModel = hiltViewModel(),
    backHandlerAction: () -> Unit,
    goToDetail: (CharacterDomain) -> Unit
) {

    val state = viewModel.state.collectAsState()
    val charactersPagingItems = viewModel.charactersPagingDataFlow.collectAsLazyPagingItems()

    CharactersListContent(
        state = state,
        goToDetail = goToDetail,
        backHandlerAction = { backHandlerAction() },
        tryAgainAction = viewModel::setErrorCharacter,
        onFavoriteClickedAction = viewModel::onFavoriteClicked,
        lazyPagingItems = charactersPagingItems
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersListContent(
    state: State<CharactersViewModel.UiState>,
    modifier: Modifier = Modifier,
    goToDetail: (CharacterDomain) -> Unit,
    backHandlerAction: () -> Unit,
    onFavoriteClickedAction: KSuspendFunction1<CharacterDomain, Unit>,
    lazyPagingItems: LazyPagingItems<CharacterDomain>,
    tryAgainAction: KFunction1<Boolean, Unit>
) {

    val scrollState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(scrollState)
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var mediatorState = lazyPagingItems.loadState.mediator

    RickAndMortyComposeLayout {

        Scaffold(
            snackbarHost = { SnackbarHost(snackBarHostState) },
            topBar = {
                TopAppBarCustom(
                    scrollBehavior = scrollBehavior,
                    title = stringResource(id = R.string.app_name)
                )
            }
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {

                if (mediatorState?.refresh is LoadState.Error || mediatorState?.append is LoadState.Error)
                    tryAgainAction(true)

                if (state.value.errorPaging) {

                    if (lazyPagingItems.itemCount > 0) {

                        snackBarHostState.SnackBarCustom(
                            title = R.string.title_error_characters_snack_bar,
                            action = R.string.action_error_characters_snack_bar,
                            key = arrayOf(
                                state.value.errorPaging
                            ),
                            coroutineScope = coroutineScope,
                            performAction = {
                                tryAgainAction(false)
                                lazyPagingItems.retry()
                            },
                            performDismissed = {},
                            duration = SnackbarDuration.Indefinite
                        )

                        DisplayItems(
                            modifier = modifier,
                            lazyPagingItems = lazyPagingItems,
                            goToDetail = goToDetail,
                            onFavoriteClickedAction = onFavoriteClickedAction
                        )

                    } else {

                        ErrorScreen(
                            customError = null,
                            setTryAgainState = {
                                tryAgainAction(false)
                                lazyPagingItems.retry()
                            },
                            onBackHandlerAction = backHandlerAction
                        )
                    }

                } else {

                    DisplayItems(
                        modifier = modifier,
                        lazyPagingItems = lazyPagingItems,
                        goToDetail = goToDetail,
                        onFavoriteClickedAction = onFavoriteClickedAction
                    )

                }

            }

        }

        if (lazyPagingItems.loadState.refresh is LoadState.Loading)
            CircularProgressIndicatorCustom()

    }

}

@Composable
private fun DisplayItems(
    modifier: Modifier,
    lazyPagingItems: LazyPagingItems<CharacterDomain>,
    goToDetail: (CharacterDomain) -> Unit,
    onFavoriteClickedAction: KSuspendFunction1<CharacterDomain, Unit>
) {

    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it.id },
        ) { index ->
            val character = lazyPagingItems[index]
            if (character != null) {
                CharacterItem(
                    character = character,
                    clickOnRow = { goToDetail(character) },
                    onFavoriteAction = {
                        coroutineScope.launch {
                            onFavoriteClickedAction.invoke(
                                character
                            )
                        }
                    }
                )
            }
        }
    }
}