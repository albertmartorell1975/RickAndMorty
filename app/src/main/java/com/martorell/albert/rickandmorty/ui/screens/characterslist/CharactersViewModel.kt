package com.martorell.albert.rickandmorty.ui.screens.characterslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.martorell.albert.domain.characters.app.CharacterDomain
import com.martorell.albert.usecases.characters.CharactersInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val interactors: CharactersInteractors) :
    ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    data class UiState(
        val loading: Boolean = false,
        val charactersPaging: PagingData<CharacterDomain>? = null,
        val errorPaging: Boolean = false
    )

    var charactersPagingDataFlow: Flow<PagingData<CharacterDomain>> =
        interactors.getPagingCharactersUseCase()
            .cachedIn(viewModelScope)

    fun setErrorCharacter(error: Boolean) {

        _state.update {
            it.copy(
                loading = false,
                errorPaging = error
            )
        }
    }

    suspend fun onFavoriteClicked(character: CharacterDomain) {

        interactors.switchFavoriteUseCase.invoke(character)

    }

}