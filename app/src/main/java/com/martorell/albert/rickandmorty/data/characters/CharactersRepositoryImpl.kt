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

            if (charactersLocalDataSource.isEmpty()) {

                charactersLocalDataSource.saveCharacters(characters.results)
                characters.results.listFromResponseToDomain()

            } else {

                if (charactersLocalDataSource.getFavorites().isNotEmpty()) {

                    val favoriteCharacters = charactersLocalDataSource.getFavorites()
                    charactersLocalDataSource.saveCharacters(characters.results)
                    charactersLocalDataSource.updateFavorite(
                        charactersToUpdate = favoriteCharacters,
                        favoriteStatus = true
                    )
                    characters.results.listFromResponseToDomain()

                } else {

                    charactersLocalDataSource.saveCharacters(characters.results)
                    characters.results.listFromResponseToDomain()

                }

            }

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