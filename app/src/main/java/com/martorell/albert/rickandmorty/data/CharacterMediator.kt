package com.martorell.albert.rickandmorty.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.martorell.albert.data.sources.characters.CharactersServerDataSource
import com.martorell.albert.rickandmorty.data.characters.listFromServerToDB
import com.martorell.albert.rickandmorty.db.RickAndMortyDatabase
import com.martorell.albert.rickandmorty.db.model.CharacterDB
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CharacterMediator @Inject constructor(
    private val database: RickAndMortyDatabase,
    private val serverDataSource: CharactersServerDataSource
) : RemoteMediator<Int, CharacterDB>() {

    var listOfFavorites: List<Int> = emptyList()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterDB>
    ): MediatorResult {

        val charactersDao = database.charactersDao()

        return try {

            val loadKey = when (loadType) {

                LoadType.APPEND -> {
                    // Retrieve next offset from database
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }

                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.REFRESH ->
                    1

            }

            val characters = serverDataSource.getCharacters(page = loadKey)
            val charactersDB = characters.results.listFromServerToDB(loadKey)

            database.withTransaction {

                if (loadType == LoadType.REFRESH) {

                    if (charactersDao.characterCount() == 0) {

                        charactersDao.saveCharacters(charactersDB)

                    } else {

                        listOfFavorites = charactersDao.getFavorites()
                        charactersDao.clearAll()

                        if (listOfFavorites.isNotEmpty()) {

                            charactersDao.saveCharacters(charactersDB)
                            charactersDao.updateFavoriteStatusMultipleCharacters(
                                ids = listOfFavorites,
                                favorite = true
                            )
                        } else {

                            charactersDao.saveCharacters(charactersDB)

                        }

                    }

                } else {

                    if (listOfFavorites.isNotEmpty()) {

                        charactersDao.saveCharacters(charactersDB)
                        charactersDao.updateFavoriteStatusMultipleCharacters(
                            ids = listOfFavorites,
                            favorite = true
                        )
                    } else {

                        charactersDao.saveCharacters(charactersDB)

                    }

                }

            }

            // Check if end of pagination reached
            MediatorResult.Success(endOfPaginationReached = characters.results.size < state.config.pageSize)

        } catch (e: IOException) {

            MediatorResult.Error(e)

        } catch (e: HttpException) {

            MediatorResult.Error(e)

        }

    }

}