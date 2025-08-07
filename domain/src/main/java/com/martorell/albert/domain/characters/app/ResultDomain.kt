package com.martorell.albert.domain.characters.app

data class ResultDomain(
    val info: InfoDomain,
    val results: List<CharacterDomain>
)
