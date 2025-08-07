package com.martorell.albert.rickandmorty.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.martorell.albert.rickandmorty.db.dao.CharacterDao
import com.martorell.albert.rickandmorty.db.model.CharacterDB

@Database(
    entities = [CharacterDB::class],
    version = 1,
    exportSchema = false
)
abstract class RickAndMortyDatabase : RoomDatabase() {

    abstract fun charactersDao(): CharacterDao

}