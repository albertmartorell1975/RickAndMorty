package com.martorell.albert.rickandmorty.ui.screens.characterslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.right
import com.martorell.albert.data.CustomError
import com.martorell.albert.data.CustomErrorFlow
import com.martorell.albert.data.ResultResponse
import com.martorell.albert.data.toCustomErrorFlow
import com.martorell.albert.domain.characters.app.CharacterDomain
import com.martorell.albert.usecases.characters.CharactersInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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
        val characters: ResultResponse<List<CharacterDomain>> = Either.Right(emptyList()),
        val error: CustomError? = null,
        val errorFlow: CustomErrorFlow? = null,
        val showAlertDialog: Boolean = false
    )

    init {
        downloadCharacters()
    }

    fun downloadCharacters() {

        viewModelScope.launch {

            _state.update {
                it.copy(
                    loading = true,
                    error = null,
                    errorFlow = null,
                    showAlertDialog = false
                )
            }

            val result = interactors.downloadCharactersUseCase.invoke()

            result.fold({
                _state.update { updatedState ->
                    updatedState.copy(
                        loading = false,
                        error = it
                    )
                }
            }) {

                interactors.getCharactersUseCase.invoke().catch { cause ->

                    _state.update {
                        it.copy(
                            loading = false,
                            errorFlow = cause.toCustomErrorFlow()
                        )
                    }

                }.collect { listOfCharacters ->

                    _state.update {
                        it.copy(
                            loading = false,
                            characters = listOfCharacters.right()
                        )
                    }

                }
            }

        }
    }

    fun showAlertDialog(){

        _state.update { updatedState ->
            updatedState.copy(
                showAlertDialog = true
            )
        }
    }

    fun hideAlertDialog(){

        _state.update { updatedState ->
            updatedState.copy(
                showAlertDialog = false
            )
        }
    }
}