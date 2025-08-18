package com.martorell.albert.usecases.characters

import com.martorell.albert.usecases.detail.SwitchFavoriteUseCase

data class CharactersInteractors(
    val getPagingCharactersUseCase: GetPagingCharactersUseCase,
    val switchFavoriteUseCase: SwitchFavoriteUseCase
)