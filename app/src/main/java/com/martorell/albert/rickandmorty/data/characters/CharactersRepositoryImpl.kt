package com.martorell.albert.rickandmorty.data.characters

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.martorell.albert.data.ResultResponse
import com.martorell.albert.data.customTryCatch
import com.martorell.albert.data.repositories.characters.CharactersRepository
import com.martorell.albert.data.sources.characters.CharactersLocalDataSource
import com.martorell.albert.data.sources.characters.CharactersServerDataSource
import com.martorell.albert.domain.characters.app.CharacterDomain
import com.martorell.albert.rickandmorty.db.model.CharacterDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharactersRepositoryImpl(
    private val charactersServerDataSource: CharactersServerDataSource,
    private val charactersLocalDataSource: CharactersLocalDataSource,
    private val characterPager: Pager<Int, CharacterDB>
) :
    CharactersRepository {

    override val listOfCharacters: Flow<List<CharacterDomain>>
        get() = charactersLocalDataSource.loadCharacters()

    override fun loadPagingAll(): Flow<PagingData<CharacterDomain>> =

        characterPager.flow.map { pagingData ->
            pagingData.map { it.fromDBToDomain() }
        }

    override suspend fun loadCharacterById(id: Int): ResultResponse<CharacterDomain> =

        customTryCatch {
            val character = charactersLocalDataSource.loadCharacterById(id)
            character
        }

    override suspend fun switchFavorite(characterDomain: CharacterDomain) {

        charactersLocalDataSource.updateFavorite(
            charactersToUpdate = listOf(characterDomain.id),
            favoriteStatus = !characterDomain.favorite
        )

    }

}