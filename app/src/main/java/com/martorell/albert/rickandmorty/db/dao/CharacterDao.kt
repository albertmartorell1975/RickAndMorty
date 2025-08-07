package com.martorell.albert.rickandmorty.db.dao

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

}