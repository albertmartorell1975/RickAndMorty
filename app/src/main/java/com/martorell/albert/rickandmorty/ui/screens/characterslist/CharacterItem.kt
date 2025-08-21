package com.martorell.albert.rickandmorty.ui.screens.characterslist

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.martorell.albert.domain.characters.app.CharacterDomain
import com.martorell.albert.rickandmorty.R
import com.martorell.albert.rickandmorty.ui.RickAndMortyComposeLayout
import com.martorell.albert.rickandmorty.utils.debugPlaceholder

@Composable
fun CharacterItem(
    character: CharacterDomain,
    clickOnRow: () -> Unit,
    onFavoriteAction: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .clickable { clickOnRow() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_spacer))
    ) {

        AsyncImage(
            modifier = Modifier
                .height(80.dp)
                .width(80.dp),
            error = debugPlaceholder(R.drawable.ic_launcher_foreground),
            model = ImageRequest.Builder(LocalContext.current)
                .data(character.image).crossfade(true).build(),
            contentDescription = character.name,
            contentScale = ContentScale.Crop
        )

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(dimensionResource(R.dimen.padding_small)),
            text = character.name,
            color = MaterialTheme.typography.titleLarge.color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Icon(
            imageVector = if (character.favorite)
                Icons.Default.Favorite
            else
                Icons.Default.FavoriteBorder,
            contentDescription = stringResource(R.string.favourite_character),
            modifier = Modifier
                .padding(end = dimensionResource(R.dimen.padding_medium))
                .height(30.dp)
                .width(30.dp)
                .clickable {
                    onFavoriteAction()
                },
            tint = MaterialTheme.colorScheme.outline
        )

    }

}

@Composable
@Preview(
    showBackground = true,
    widthDp = 600,
    heightDp = 200,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
fun CharacterItemPreview() {

    RickAndMortyComposeLayout {

        CharacterItem(
            clickOnRow = {},
            onFavoriteAction = {},
            character = CharacterDomain(
                created = "",
                gender = "Male",
                id = 1,
                image = "A not valid image url",
                name = "Hello Doctor",
                species = "Alien",
                status = "I am feel good",
                type = "Type",
                url = "Url",
                favorite = false
            )
        )
    }

}