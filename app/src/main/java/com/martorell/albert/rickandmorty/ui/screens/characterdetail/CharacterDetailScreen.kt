package com.martorell.albert.rickandmorty.ui.screens.characterdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.martorell.albert.rickandmorty.R
import com.martorell.albert.rickandmorty.ui.RickAndMortyComposeLayout
import com.martorell.albert.rickandmorty.ui.navigation.shared.TopAppBarCustom
import com.martorell.albert.rickandmorty.ui.screens.shared.CircularProgressIndicatorCustom
import com.martorell.albert.rickandmorty.ui.screens.shared.DefaultTextView
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
    modifier: Modifier = Modifier,
    viewModel: CharacterDetailViewModel = hiltViewModel(),
    navigationUpAction: () -> Unit,
    backHandlerAction: () -> Unit
) {

    val state = viewModel.state.collectAsState()

    CharacterDetailContent(
        modifier = modifier,
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
    modifier: Modifier = Modifier,
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

            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.padding_small),
                    Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

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
                    DefaultTextView(
                        contentFix = "",
                        contentDynamic = state.value.character?.name.toString(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(dimensionResource(R.dimen.medium_spacer)))

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(state.value.character?.image).crossfade(true).build(),
                        contentDescription = state.value.character?.name.toString(),
                        modifier = Modifier
                            .height(200.dp)
                            .width(
                                200.dp
                            ),
                        contentScale = ContentScale.Crop
                    )

                    var userClickedOnFab by remember { mutableIntStateOf(0) }
                    val icon by produceState<ImageVector?>(
                        initialValue = null,
                        key1 = userClickedOnFab
                    ) {
                        value = if (isCharacterFavoriteAction()) {
                            Icons.Default.Favorite
                        } else
                            Icons.Default.FavoriteBorder
                    }

                    icon?.run {
                        Icon(
                            imageVector = this,
                            contentDescription = stringResource(R.string.favourite_character),
                            modifier = Modifier.clickable {
                                coroutineScope.launch { onFavoriteClickedAction() }
                                userClickedOnFab += 1
                            },
                            tint = Color.Black
                        )
                    }

                    state.value.character?.type?.also { info ->

                        DefaultTextView(
                            contentFix = stringResource(R.string.character_type),
                            contentDynamic = if (info.isNotEmpty())
                                info
                            else
                                stringResource(R.string.without_info)
                        )
                    } ?: run {
                        DefaultTextView(
                            contentFix = stringResource(R.string.character_type),
                            contentDynamic = stringResource(R.string.without_info),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    state.value.character?.gender?.also { info ->

                        DefaultTextView(
                            contentFix = stringResource(R.string.character_gender),
                            contentDynamic = if (info.isNotEmpty())
                                info
                            else
                                stringResource(R.string.without_info)
                        )
                    } ?: run {
                        DefaultTextView(
                            contentFix = stringResource(R.string.character_type),
                            contentDynamic = stringResource(R.string.without_info),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    state.value.character?.species?.also { info ->

                        DefaultTextView(
                            contentFix = stringResource(R.string.character_specie),
                            contentDynamic = if (info.isNotEmpty())
                                info
                            else
                                stringResource(R.string.without_info)
                        )
                    } ?: run {
                        DefaultTextView(
                            contentFix = stringResource(R.string.character_type),
                            contentDynamic = stringResource(R.string.without_info),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    state.value.character?.status?.also { info ->

                        DefaultTextView(
                            contentFix = stringResource(R.string.character_status),
                            contentDynamic = if (info.isNotEmpty())
                                info
                            else
                                stringResource(R.string.without_info)
                        )
                    } ?: run {
                        DefaultTextView(
                            contentFix = stringResource(R.string.character_type),
                            contentDynamic = stringResource(R.string.without_info),
                            fontWeight = FontWeight.Bold
                        )
                    }

                }

            }

            if (state.value.loading)
                CircularProgressIndicatorCustom()

        }
    }

}