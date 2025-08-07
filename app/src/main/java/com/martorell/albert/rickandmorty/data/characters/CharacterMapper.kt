package com.martorell.albert.rickandmorty.data.characters

import com.martorell.albert.domain.characters.app.CharacterDomain
import com.martorell.albert.domain.characters.server.CharacterResponse
import com.martorell.albert.rickandmorty.db.model.CharacterDB

fun List<CharacterResponse>.fromServerToDB(): List<CharacterDB> {

    val characterDBList = mutableListOf<CharacterDB>()

    for (character in this) {
        val characterDB = CharacterDB(
            id = character.id,
            name = character.name,
            created = character.created,
            gender = character.gender,
            image = character.image,
            species = character.species,
            status = character.status,
            type = character.type,
            url = character.url
        )

        characterDBList.add(characterDB)
    }

    return characterDBList

}

fun List<CharacterDB>.fromDBToDomain(): List<CharacterDomain> {

    val characterDomainList = mutableListOf<CharacterDomain>()

    for (characterDB in this) {
        val characterDomain = CharacterDomain(
            id = characterDB.id,
            name = characterDB.name,
            created = characterDB.created,
            gender = characterDB.gender,
            image = characterDB.image,
            species = characterDB.species,
            status = characterDB.status,
            type = characterDB.type,
            url = characterDB.url
        )

        characterDomainList.add(characterDomain)
    }

    return characterDomainList

}

fun List<CharacterResponse>.fromResponseToDomain(): List<CharacterDomain> {

    val characterDomainList = mutableListOf<CharacterDomain>()

    for (character in this) {
        val characterDomain = CharacterDomain(
            id = character.id,
            name = character.name,
            created = character.created,
            gender = character.gender,
            image = character.image,
            species = character.species,
            status = character.status,
            type = character.type,
            url = character.url
        )

        characterDomainList.add(characterDomain)
    }

    return characterDomainList

}