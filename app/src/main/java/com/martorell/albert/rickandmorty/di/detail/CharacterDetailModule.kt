package com.martorell.albert.rickandmorty.di.detail

import com.martorell.albert.usecases.characters.GetCharactersUseCase
import com.martorell.albert.usecases.detail.CharacterDetailInteractors
import com.martorell.albert.usecases.detail.LoadCharacterByIdUseCase
import com.martorell.albert.usecases.detail.SwitchFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class CharacterDetailModule {

    @Provides
    fun providesCharacterDetailInteractors(
        loadCharacterByIdUseCase: LoadCharacterByIdUseCase,
        getCharactersUseCase: GetCharactersUseCase,
        switchFavoriteUseCase: SwitchFavoriteUseCase
    ) = CharacterDetailInteractors(
        loadCharacterByIdUseCase = loadCharacterByIdUseCase,
        getCharactersUseCase = getCharactersUseCase,
        switchFavoriteUseCase=switchFavoriteUseCase
    )
}