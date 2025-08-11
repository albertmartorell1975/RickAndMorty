package com.martorell.albert.usecases.detail

import com.martorell.albert.data.ResultResponse
import com.martorell.albert.data.repositories.characters.CharactersRepository
import com.martorell.albert.domain.characters.app.CharacterDomain
import javax.inject.Inject

class LoadCharacterByIdUseCase @Inject constructor(private val charactersRepository: CharactersRepository) {

    suspend fun invoke(id: Int): ResultResponse<CharacterDomain> =
        charactersRepository.loadCharacterById(id)

}