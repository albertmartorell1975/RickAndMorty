package com.martorell.albert.usecases.characters

import androidx.paging.PagingData
import com.martorell.albert.data.repositories.characters.CharactersRepository
import com.martorell.albert.domain.characters.app.CharacterDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPagingCharactersUseCase @Inject constructor(private val charactersRepository: CharactersRepository) {

    operator fun invoke(): Flow<PagingData<CharacterDomain>> {
        return charactersRepository.loadPagingAll()
            .flowOn(Dispatchers.IO)
    }

}