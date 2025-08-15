package com.martorell.albert.data.repositories.characters

import androidx.paging.PagingData
import com.martorell.albert.data.ResultResponse
import com.martorell.albert.domain.characters.app.CharacterDomain
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {

    val listOfCharacters: Flow<List<CharacterDomain>>
    suspend fun loadCharacterById(id: Int): ResultResponse<CharacterDomain>
    suspend fun switchFavorite(characterDomain: CharacterDomain)
    fun loadPagingAll(): Flow<PagingData<CharacterDomain>>

}