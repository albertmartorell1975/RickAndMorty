package com.martorell.albert.domain.characters.server

data class RickAndMortyResponse(
    val info: InfoResponse,
    val results: List<CharacterResponse>
)