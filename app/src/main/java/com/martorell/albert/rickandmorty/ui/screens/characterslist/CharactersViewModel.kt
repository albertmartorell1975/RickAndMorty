package com.martorell.albert.rickandmorty.ui.screens.characterslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martorell.albert.domain.characters.app.CharacterDomain
import com.martorell.albert.usecases.characters.CharactersInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val interactors: CharactersInteractors) :
    ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    data class UiState(
        val loading: Boolean = false,
        val characters: List<CharacterDomain> = emptyList<CharacterDomain>()
    )

    init {

        _state.update {
            it.copy(
                loading = true
            )
        }

        viewModelScope.launch {
            interactors.downloadCharactersUseCase.invoke()
            interactors.getCharactersUseCase.invoke().collect { listOfCharacters ->

                _state.update {
                    it.copy(
                        loading = false,
                        characters = listOfCharacters
                    )
                }

            }
        }

    }

    override fun onCleared() {
        super.onCleared()
    }
}