package com.martorell.albert.data.sources.characters

import com.martorell.albert.domain.characters.app.CharacterDomain
import com.martorell.albert.domain.characters.server.CharacterResponse
import kotlinx.coroutines.flow.Flow

interface CharactersLocalDataSource {

    suspend fun saveCharacters(characters: List<CharacterResponse>)
    fun loadCharacters(): Flow<List<CharacterDomain>>
    suspend fun loadCharacterById(id: Int): CharacterDomain
}