package com.martorell.albert.data.repositories.characters

import com.martorell.albert.data.ResultResponse
import com.martorell.albert.domain.characters.app.CharacterDomain
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {

    val listOfCharacters: Flow<List<CharacterDomain>>
    suspend fun downloadCharacters(): ResultResponse<List<CharacterDomain>>
    suspend fun loadCharacterById(id: Int): ResultResponse<CharacterDomain>

}