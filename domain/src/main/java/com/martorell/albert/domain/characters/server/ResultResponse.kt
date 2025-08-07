package com.martorell.albert.domain.characters.server

data class ResultResponse(
    val info: InfoResponse,
    val results: List<CharacterResponse>
)