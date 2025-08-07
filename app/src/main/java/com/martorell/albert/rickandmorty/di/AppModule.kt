package com.martorell.albert.rickandmorty.di

import android.app.Application
import androidx.room.Room
import com.martorell.albert.data.sources.characters.CharactersServerDataSource
import com.martorell.albert.rickandmorty.db.RickAndMortyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    companion object {
        private const val ROOM_DATABASE = "RickAndMortyDatabase"
        private const val BASE_URL = "https://rickandmortyapi.com/api/"
    }

    @Provides
    @Singleton
    fun providesDatabase(app: Application): RickAndMortyDatabase = Room.databaseBuilder(
        app,
        RickAndMortyDatabase::class.java,
        ROOM_DATABASE
    ).fallbackToDestructiveMigration(true)
        .build() //fallbackToDestructiveMigration: each time there is an increase version we enable a destructive migration (database is cleared)

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun providesCharactersServerDataSource(retrofit: Retrofit): CharactersServerDataSource =
        retrofit.create(CharactersServerDataSource::class.java)

}