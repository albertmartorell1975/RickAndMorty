package com.martorell.albert.data.repositories.characters

import com.martorell.albert.data.CustomErrorFlow
import com.martorell.albert.domain.characters.app.CharacterDomain
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {

    val listOfCharacters: Flow<List<CharacterDomain>>
    suspend fun getCharacters(): CustomErrorFlow?

}