package com.martorell.albert.usecases.detail

import com.martorell.albert.data.repositories.characters.CharactersRepository
import com.martorell.albert.domain.characters.app.CharacterDomain
import javax.inject.Inject

class SwitchFavoriteUseCase @Inject constructor(private val charactersRepository: CharactersRepository) {

    suspend fun invoke(character: CharacterDomain) {
        charactersRepository.switchFavorite(character)
    }

}