package com.martorell.albert.rickandmorty.data.characters

import com.martorell.albert.data.sources.characters.CharactersLocalDataSource
import com.martorell.albert.domain.characters.app.CharacterDomain
import com.martorell.albert.domain.characters.server.CharacterResponse
import com.martorell.albert.rickandmorty.db.RickAndMortyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CharactersRoomDataSource(db: RickAndMortyDatabase) : CharactersLocalDataSource {

    private val charactersDao = db.charactersDao()

    override suspend fun saveCharacters(characters: List<CharacterResponse>) {

        withContext(Dispatchers.IO) {
            charactersDao.saveCharacters(characters.listFromServerToDB())
        }

    }

    override fun loadCharacters(): Flow<List<CharacterDomain>> =
        charactersDao.getAll().map { it.listFromDBToDomain() }

    override suspend fun loadCharacterById(id: Int): CharacterDomain =
        withContext(Dispatchers.IO) { charactersDao.getCharacterById(id).fromDBToDomain() }

    override suspend fun isEmpty(): Boolean =

        withContext(Dispatchers.IO) {
            charactersDao.characterCount() == 0
        }

    override suspend fun getFavorites(): List<Int> =

        withContext(Dispatchers.IO) {
            charactersDao.getFavorites()
        }

    override suspend fun updateFavorite(charactersToUpdate: List<Int>, favoriteStatus: Boolean) {

        withContext(Dispatchers.IO) {
            charactersDao.updateFavoriteStatusMultipleCharacters(
                ids = charactersToUpdate,
                favorite = favoriteStatus
            )
        }

    }

}