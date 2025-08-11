package com.martorell.albert.usecases.detail

import com.martorell.albert.usecases.characters.GetCharactersUseCase

data class CharacterDetailInteractors(
    val loadCharacterByIdUseCase: LoadCharacterByIdUseCase,
    val getCharactersUseCase: GetCharactersUseCase,
    val switchFavoriteUseCase: SwitchFavoriteUseCase
)