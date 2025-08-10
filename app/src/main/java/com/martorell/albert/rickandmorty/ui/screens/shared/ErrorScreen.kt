package com.martorell.albert.rickandmorty.ui.screens.shared

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.martorell.albert.data.CustomError
import com.martorell.albert.rickandmorty.R

@Composable
fun <T> ErrorScreen(
    customError: T,
    setTryAgainState: () -> Unit,
    onBackHandlerAction: () -> Unit
) {

    val message = when (customError) {
        is CustomError.Connectivity -> stringResource(R.string.connectivity_error)
        is CustomError.Server -> stringResource(R.string.server_error)
        is CustomError.Unknown -> stringResource(R.string.unknown_error)
        else -> stringResource(R.string.unknown_error)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = message,
            modifier = Modifier.size(128.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp
            ),
            text = message,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.standard_height)))
        Button(onClick = { setTryAgainState() }) {
            Text(text = stringResource(R.string.train_again))
        }

        BackHandler {
            onBackHandlerAction()
        }

    }

}

@Composable
@Preview(showBackground = true)
fun PreviewErrorScreen() {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Missatge d'error",
            modifier = Modifier.size(128.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp
            ),
            text = "Missatge d'error per veure l'amplada del text a la pantalla",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(dimensionResource(R.dimen.standard_height)))
        Button(onClick = {}) {
            Text(text = "Try again")
        }

    }

}