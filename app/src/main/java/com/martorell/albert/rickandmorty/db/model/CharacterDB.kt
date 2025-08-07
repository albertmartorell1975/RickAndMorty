package com.martorell.albert.rickandmorty.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterDB(
    @PrimaryKey
    val id: Int,
    val created: String,
    val gender: String,
    val image: String,
    val name: String,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)