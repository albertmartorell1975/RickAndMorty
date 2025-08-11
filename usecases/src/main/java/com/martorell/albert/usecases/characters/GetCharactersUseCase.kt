package com.martorell.albert.usecases.characters


import com.martorell.albert.data.repositories.characters.CharactersRepository
import com.martorell.albert.domain.characters.app.CharacterDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(private val charactersRepository: CharactersRepository) {

    fun invoke(): Flow<List<CharacterDomain>> =
        charactersRepository.listOfCharacters

}