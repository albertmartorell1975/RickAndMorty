package com.martorell.albert.rickandmorty.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.martorell.albert.rickandmorty.db.model.CharacterDB
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCharacters(character: List<CharacterDB>)

    @Query("SELECT * FROM CharacterDB")
    fun getAll(): Flow<List<CharacterDB>>

    @Query("SELECT * FROM CharacterDB")
    fun getAllPaging(): PagingSource<Int, CharacterDB>

    @Query("DELETE FROM CharacterDB")
    fun clearAll()

    @Query(value = "SELECT * FROM CharacterDB WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterDB

    @Query("SELECT COUNT(id) FROM CharacterDB")
    suspend fun characterCount(): Int

    @Query("SELECT id FROM CharacterDB WHERE favorite=true")
    suspend fun getFavorites(): List<Int>

    @Query("UPDATE CharacterDB SET favorite =:favorite WHERE id IN (:ids)")
    suspend fun updateFavoriteStatusMultipleCharacters(ids: List<Int>, favorite: Boolean)
}