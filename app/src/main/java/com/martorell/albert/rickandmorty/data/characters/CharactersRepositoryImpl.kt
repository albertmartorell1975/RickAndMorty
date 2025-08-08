package com.martorell.albert.rickandmorty.data.characters

import com.martorell.albert.data.ResultResponse
import com.martorell.albert.data.customTryCatch
import com.martorell.albert.data.repositories.characters.CharactersRepository
import com.martorell.albert.data.sources.characters.CharactersLocalDataSource
import com.martorell.albert.data.sources.characters.CharactersServerDataSource
import com.martorell.albert.domain.characters.app.CharacterDomain
import kotlinx.coroutines.flow.Flow

class CharactersRepositoryImpl(
    private val charactersServerDataSource: CharactersServerDataSource,
    private val charactersLocalDataSource: CharactersLocalDataSource
) :
    CharactersRepository {

    override val listOfCharacters: Flow<List<CharacterDomain>>
        get() = charactersLocalDataSource.loadCharacters()

    override suspend fun downloadCharacters(): ResultResponse<List<CharacterDomain>> =

        customTryCatch {
            val characters = charactersServerDataSource.getCharacters()
            charactersLocalDataSource.saveCharacters(characters.results)
            characters.results.listFromResponseToDomain()
        }

    override suspend fun loadCharacterById(id: Int): ResultResponse<CharacterDomain> =

        customTryCatch {
            val character = charactersLocalDataSource.loadCharacterById(id)
            character
        }

}