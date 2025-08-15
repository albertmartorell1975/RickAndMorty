package com.martorell.albert.usecases.characters

data class CharactersInteractors(
    val downloadCharactersUseCase: DownloadCharactersUseCase,
    val getCharactersUseCase: GetCharactersUseCase,
    val getPagingCharactersUseCase: GetPagingCharactersUseCase
)