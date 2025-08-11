package com.martorell.albert.domain.characters.server

data class InfoResponse(
    val count: Int,
    val next: String,
    val pages: Int,
    val prev: String?
)