package com.martorell.albert.rickandmorty.ui.screens.characterslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.martorell.albert.domain.characters.app.CharacterDomain
import com.martorell.albert.rickandmorty.R
import com.martorell.albert.rickandmorty.utils.previewAsyncImageCoil

@Composable
fun CharacterItem(
    modifier: Modifier = Modifier,
    character: CharacterDomain,
    clickOnRow: () -> Unit,
    onFavoriteAction: () -> Unit
) {

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .clickable { clickOnRow() }) {

        // Create references for the composable to constrain
        val (favorite, name, specie, characterIcon) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(name) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(favorite.start, margin = 16.dp)
                    bottom.linkTo(characterIcon.top, margin = 16.dp)
                    width = Dimension.fillToConstraints
                },
            text = character.name,
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
                .constrainAs(favorite) {
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    width = Dimension.fillToConstraints
                }
                .clickable {
                    onFavoriteAction()
                },
            tint = Color.Black
        )
        AsyncImage(
            modifier = Modifier
                .height(80.dp)
                .width(80.dp)
                .constrainAs(characterIcon) {
                    top.linkTo(name.bottom, margin = 8.dp)
                    end.linkTo(specie.start, margin = 8.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                },
            model = ImageRequest.Builder(LocalContext.current)
                .data(character.image).crossfade(true).build(),
            contentDescription = character.name,
            contentScale = ContentScale.Crop
        )

        Text(
            color = Color.DarkGray,
            modifier = Modifier.constrainAs(specie) {
                top.linkTo(name.bottom, margin = 8.dp)
                bottom.linkTo(characterIcon.bottom)
                start.linkTo(characterIcon.end)
                end.linkTo(parent.end, margin = 16.dp)
                width = Dimension.fillToConstraints
            },
            text = character.species,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium
        )

    }

}

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview(showBackground = true, widthDp = 600, heightDp = 200)
fun CharacterItemPreview() {

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        // Create references for the composables to constrain
        val (favorite, characterName, specie, characterIcon) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(characterName) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(favorite.start, margin = 16.dp)
                    width = Dimension.fillToConstraints
                },
            text = "Rick and Morty blà, blà.......",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Icon(
            Icons.Default.FavoriteBorder,
            contentDescription = stringResource(R.string.favourite_character),
            modifier = Modifier
                .constrainAs(favorite) {
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    width = Dimension.fillToConstraints
                }
                .clickable {},
            tint = Color.Black
        )

        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewAsyncImageCoil) {
            AsyncImage(
                modifier = Modifier
                    .height(40.dp)
                    .width(40.dp)
                    .constrainAs(characterIcon) {
                        top.linkTo(characterName.bottom, margin = 8.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    },
                model = "https://example.com/image.jpg",
                contentDescription = "La imatge",
                contentScale = ContentScale.Crop
            )
        }

        Text(
            color = Color.DarkGray,
            modifier = Modifier.constrainAs(specie) {
                top.linkTo(characterName.bottom, margin = 8.dp)
                start.linkTo(characterIcon.end, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
                width = Dimension.fillToConstraints
            },
            text = "Tipus d'espècie",
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium
        )

    }

}