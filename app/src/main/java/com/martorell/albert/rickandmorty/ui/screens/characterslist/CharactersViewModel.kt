package com.martorell.albert.rickandmorty.ui.screens.characterslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martorell.albert.usecases.characters.CharactersInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val interactors: CharactersInteractors): ViewModel() {

    init {

        viewModelScope.launch{
            interactors.downloadCharactersUseCase.invoke()
            interactors.getCharactersUseCase.invoke().collect { it ->

                val k = it
                val s = ""

            }
        }

    }

    override fun onCleared() {
        super.onCleared()
    }
}