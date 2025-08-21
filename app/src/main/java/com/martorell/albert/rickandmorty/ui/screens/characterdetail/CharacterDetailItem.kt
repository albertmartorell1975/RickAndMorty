package com.martorell.albert.rickandmorty.ui.screens.characterdetail

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.martorell.albert.domain.characters.app.CharacterDomain
import com.martorell.albert.rickandmorty.R
import com.martorell.albert.rickandmorty.ui.RickAndMortyComposeLayout
import com.martorell.albert.rickandmorty.ui.screens.shared.DefaultTextView
import com.martorell.albert.rickandmorty.utils.debugPlaceholder
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

@Composable
fun CharacterDetailItem(
    modifier: Modifier = Modifier,
    character: CharacterDomain?,
    isCharacterFavoriteAction: KSuspendFunction0<Boolean>?,
    onFavoriteClickedAction: KSuspendFunction0<Unit>?
) {

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.padding_small),
            Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        DefaultTextView(
            contentFix = "",
            contentDynamic = character?.name.toString(),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        AsyncImage(
            error = debugPlaceholder(R.drawable.ic_launcher_foreground),
            model = ImageRequest.Builder(LocalContext.current)
                .data(character?.image).crossfade(true).build(),
            contentDescription = character?.name.toString(),
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
            value = if (isCharacterFavoriteAction?.invoke() ?: false) {
                Icons.Default.Favorite
            } else
                Icons.Default.FavoriteBorder
        }

        icon?.run {
            Icon(
                imageVector = this,
                contentDescription = stringResource(R.string.favourite_character),
                modifier = Modifier.clickable {
                    coroutineScope.launch { onFavoriteClickedAction?.invoke() }
                    userClickedOnFab += 1
                },
                tint = Color.Black
            )
        }

        character?.type?.also { info ->

            DefaultTextView(
                contentFix = stringResource(R.string.character_type),
                contentDynamic = info.ifEmpty { stringResource(R.string.without_info) }
            )
        } ?: run {
            DefaultTextView(
                contentFix = stringResource(R.string.character_type),
                contentDynamic = stringResource(R.string.without_info),
                fontWeight = FontWeight.Bold
            )
        }

        character?.gender?.also { info ->

            DefaultTextView(
                contentFix = stringResource(R.string.character_gender),
                contentDynamic = info.ifEmpty { stringResource(R.string.without_info) }
            )
        } ?: run {
            DefaultTextView(
                contentFix = stringResource(R.string.character_type),
                contentDynamic = stringResource(R.string.without_info),
                fontWeight = FontWeight.Bold
            )
        }

        character?.species?.also { info ->

            DefaultTextView(
                contentFix = stringResource(R.string.character_specie),
                contentDynamic = info.ifEmpty { stringResource(R.string.without_info) }
            )
        } ?: run {
            DefaultTextView(
                contentFix = stringResource(R.string.character_type),
                contentDynamic = stringResource(R.string.without_info),
                fontWeight = FontWeight.Bold
            )
        }

        character?.status?.also { info ->

            DefaultTextView(
                contentFix = stringResource(R.string.character_status),
                contentDynamic = info.ifEmpty { stringResource(R.string.without_info) }
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

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
fun CharacterDetailItemPreview() {

    val character = CharacterDomain(
        created = "",
        gender = "",
        id = 1,
        image = "A not valid image url",
        name = "Hello Doctor",
        species = "",
        status = "",
        type = "",
        url = "Url",
        favorite = false
    )

    RickAndMortyComposeLayout {

        CharacterDetailItem(
            character = character,
            isCharacterFavoriteAction = null,
            onFavoriteClickedAction = null
        )

    }

}