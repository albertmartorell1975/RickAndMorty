package com.martorell.albert.data.sources.characters

import com.martorell.albert.domain.characters.server.RickAndMortyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersServerDataSource {

    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): RickAndMortyResponse

}