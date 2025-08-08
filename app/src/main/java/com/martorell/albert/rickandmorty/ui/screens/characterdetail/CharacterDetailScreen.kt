package com.martorell.albert.rickandmorty.ui.screens.characterdetail

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        backHandlerAction = { backHandlerAction() }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailContent(
    modifier: Modifier = Modifier,
    state: State<CharacterDetailViewModel.UiState>,
    loadCharacterAction: KSuspendFunction0<Unit>,
    navigationUpAction: () -> Unit,
    backHandlerAction: () -> Unit
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

                state.value.character.fold({
                    // the error to display
                    ErrorScreen(
                        customError = it,
                        setTryAgainState = {
                            coroutineScope.launch {
                                loadCharacterAction()
                            }
                        },
                        onBackHandlerAction = backHandlerAction
                    )

                }
                )
                { characterInfo ->

                    DefaultTextView(
                        contentFix = characterInfo?.name,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(dimensionResource(R.dimen.medium_spacer)))

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(characterInfo?.image).crossfade(true).build(),
                        contentDescription = "El temps que fa",
                        modifier = Modifier
                            .height(200.dp)
                            .width(
                                200.dp
                            ),
                        contentScale = ContentScale.Crop
                    )

                    DefaultTextView(
                        contentFix = characterInfo?.gender,
                        fontWeight = FontWeight.Bold
                    )

                    DefaultTextView(
                        contentFix = stringResource(R.string.character_title),
                        contentDynamic = characterInfo?.name.toString()
                    )

                    DefaultTextView(
                        contentFix = stringResource(R.string.character_type),
                        contentDynamic = characterInfo?.type.toString(),
                        colorDynamic = Color.Red
                    )

                    DefaultTextView(
                        contentFix = stringResource(R.string.character_gender),
                        contentDynamic = characterInfo?.gender.toString(),
                        colorDynamic = Color.Blue
                    )

                    DefaultTextView(
                        contentFix = stringResource(R.string.character_specie),
                        contentDynamic = characterInfo?.species.toString()
                    )

                    DefaultTextView(
                        contentFix = stringResource(R.string.character_status),
                        contentDynamic = characterInfo?.status.toString(),
                        colorDynamic = Color.Blue
                    )
                }

            }

            if (state.value.loading)
                CircularProgressIndicatorCustom()

        }
    }
}