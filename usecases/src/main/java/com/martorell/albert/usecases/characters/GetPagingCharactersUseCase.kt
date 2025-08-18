package com.martorell.albert.usecases.characters

import androidx.paging.PagingData
import com.martorell.albert.data.repositories.characters.CharactersRepository
import com.martorell.albert.domain.characters.app.CharacterDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagingCharactersUseCase @Inject constructor(private val charactersRepository: CharactersRepository) {

    operator fun invoke(): Flow<PagingData<CharacterDomain>> {
        return charactersRepository.loadAll()
    }

}