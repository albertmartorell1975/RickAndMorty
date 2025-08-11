package com.martorell.albert.rickandmorty.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.martorell.albert.rickandmorty.ui.screens.characterdetail.CharactersDetailScreen
import com.martorell.albert.rickandmorty.ui.screens.characterslist.CharactersListScreen

@Composable
fun Navigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screens.CharactersList
    ) {

        composable<Screens.CharactersList> {
            CharactersListScreen(
                goToDetail = { navController.navigate(Screens.CharacterDetail(id = it.id)) },
                backHandlerAction = {
                    navController.popBackStack(
                        navController.graph.startDestinationId,
                        inclusive = true
                    )
                })
        }

        composable<Screens.CharacterDetail> {
            CharactersDetailScreen(
                navigationUpAction = { navController.popBackStack() },
                backHandlerAction = { navController.popBackStack() })
        }

    }

}