package com.martorell.albert.rickandmorty.ui.screens.characterdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.martorell.albert.data.CustomErrorFlow
import com.martorell.albert.data.toCustomErrorFlow
import com.martorell.albert.domain.characters.app.CharacterDomain
import com.martorell.albert.rickandmorty.ui.navigation.Screens
import com.martorell.albert.usecases.detail.CharacterDetailInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactors: CharacterDetailInteractors
) : ViewModel() {

    private val characterId =
        savedStateHandle.toRoute<Screens.CharacterDetail>().id
    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    data class UiState(
        val loading: Boolean = false,
        val character: CharacterDomain? = null,
        val errorFlow: CustomErrorFlow? = null
    )

    init {

        viewModelScope.launch {
            loadCharacter()
        }

    }

    suspend fun loadCharacter() {

        _state.update {
            it.copy(
                loading = true
            )
        }

        interactors.getCharactersUseCase.invoke().catch { cause ->

            _state.update {
                it.copy(
                    loading = false,
                    errorFlow = cause.toCustomErrorFlow()
                )
            }

        }.collect { listOfCharacters ->

            val currentCharacter = listOfCharacters.find { it.id == characterId }

            _state.update {
                it.copy(
                    loading = false,
                    character = currentCharacter
                )
            }

        }
    }

    suspend fun isCharacterFavorite(): Boolean {

        return interactors.loadCharacterByIdUseCase.invoke(characterId).fold({ false })
        { it.favorite }

    }

    suspend fun onFavoriteClicked() {

        interactors.switchFavoriteUseCase.invoke(_state.value.character!!)

    }
}