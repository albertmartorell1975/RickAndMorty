package com.martorell.albert.usecases.characters


import com.martorell.albert.data.CustomErrorFlow
import com.martorell.albert.data.repositories.characters.CharactersRepository
import javax.inject.Inject

class DownloadCharactersUseCase @Inject constructor(private val charactersRepository: CharactersRepository) {

    suspend fun invoke(): CustomErrorFlow? =
        charactersRepository.getCharacters()

}