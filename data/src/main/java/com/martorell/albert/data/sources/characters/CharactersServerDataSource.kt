package com.martorell.albert.data.sources.characters

import com.martorell.albert.domain.characters.server.ResultResponse
import retrofit2.http.GET

interface CharactersServerDataSource {

    @GET("character")
    suspend fun getCharacters(): ResultResponse

}