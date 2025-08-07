package com.martorell.albert.rickandmorty.di.characters

import com.martorell.albert.data.repositories.characters.CharactersRepository
import com.martorell.albert.data.sources.characters.CharactersLocalDataSource
import com.martorell.albert.data.sources.characters.CharactersServerDataSource
import com.martorell.albert.rickandmorty.data.characters.CharactersRepositoryImpl
import com.martorell.albert.rickandmorty.data.characters.CharactersRoomDataSource
import com.martorell.albert.rickandmorty.db.RickAndMortyDatabase
import com.martorell.albert.usecases.characters.CharactersInteractors
import com.martorell.albert.usecases.characters.DownloadCharactersUseCase
import com.martorell.albert.usecases.characters.GetCharactersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class CharactersModule {

    @Provides
    fun providesCharactersLocalDataSource(db: RickAndMortyDatabase): CharactersLocalDataSource =
        CharactersRoomDataSource(db)

    @Provides
    fun providesCharactersInteractors(
        downloadCharactersUseCase: DownloadCharactersUseCase,
        getCharactersUseCase: GetCharactersUseCase
    ) =
        CharactersInteractors(
            downloadCharactersUseCase = downloadCharactersUseCase,
            getCharactersUseCase = getCharactersUseCase
        )

    @Provides
    fun providesCharactersRepository(
        charactersServerDataSource: CharactersServerDataSource,
        charactersLocalDataSource: CharactersLocalDataSource
    ): CharactersRepository {
        return CharactersRepositoryImpl(
            charactersServerDataSource = charactersServerDataSource,
            charactersLocalDataSource = charactersLocalDataSource
        )
    }

}