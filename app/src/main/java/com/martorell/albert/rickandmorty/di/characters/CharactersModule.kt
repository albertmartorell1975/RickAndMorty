package com.martorell.albert.rickandmorty.di.characters

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.martorell.albert.data.repositories.characters.CharactersRepository
import com.martorell.albert.data.sources.characters.CharactersLocalDataSource
import com.martorell.albert.data.sources.characters.CharactersServerDataSource
import com.martorell.albert.rickandmorty.data.CharacterMediator
import com.martorell.albert.rickandmorty.data.characters.CharactersRepositoryImpl
import com.martorell.albert.rickandmorty.data.characters.CharactersRoomDataSource
import com.martorell.albert.rickandmorty.db.RickAndMortyDatabase
import com.martorell.albert.rickandmorty.db.model.CharacterDB
import com.martorell.albert.usecases.characters.CharactersInteractors
import com.martorell.albert.usecases.characters.GetPagingCharactersUseCase
import com.martorell.albert.usecases.detail.SwitchFavoriteUseCase
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
        getPagingCharactersUseCase: GetPagingCharactersUseCase,
        switchFavoriteUseCase: SwitchFavoriteUseCase
    ) =
        CharactersInteractors(
            getPagingCharactersUseCase = getPagingCharactersUseCase,
            switchFavoriteUseCase = switchFavoriteUseCase
        )

    @Provides
    fun providesCharactersRepository(
        charactersLocalDataSource: CharactersLocalDataSource,
        characterPager: Pager<Int, CharacterDB>
    ): CharactersRepository {
        return CharactersRepositoryImpl(
            charactersLocalDataSource = charactersLocalDataSource,
            characterPager = characterPager
        )

    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    fun providesCharacterPager(
        database: RickAndMortyDatabase,
        serverDataSource: CharactersServerDataSource
    ): Pager<Int, CharacterDB> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            remoteMediator = CharacterMediator(
                database = database,
                serverDataSource = serverDataSource
            ),
            pagingSourceFactory = {
                database.charactersDao().getAllPaging()
            },
        )
    }

}