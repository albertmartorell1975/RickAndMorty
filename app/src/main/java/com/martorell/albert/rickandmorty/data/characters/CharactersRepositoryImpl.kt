package com.martorell.albert.rickandmorty.data.characters

import com.martorell.albert.data.CustomErrorFlow
import com.martorell.albert.data.customFlowTryCatch
import com.martorell.albert.data.repositories.characters.CharactersRepository
import com.martorell.albert.data.sources.characters.CharactersLocalDataSource
import com.martorell.albert.data.sources.characters.CharactersServerDataSource
import com.martorell.albert.domain.characters.app.CharacterDomain
import kotlinx.coroutines.flow.Flow

class CharactersRepositoryImpl(
    private val charactersServerDataSource: CharactersServerDataSource,
    private val charactersLocalDataSource: CharactersLocalDataSource
) :
    CharactersRepository {

    override val listOfCharacters: Flow<List<CharacterDomain>>
        get() = charactersLocalDataSource.loadCharacters()

    override suspend fun getCharacters(): CustomErrorFlow? =
        customFlowTryCatch {

            // per provocar error fem que getCharacters retorni List<Characters>.
            val characters = charactersServerDataSource.getCharacters()
            charactersLocalDataSource.saveCharacters(characters.results)

        }

}

/* override suspend fun loadCityCurrentWeather(
     latitude: String,
     longitude: String
 ): CustomErrorFlow? = customFlowTryCatch {

     val cityServer = cityWeatherServerDataSource.getWeather(
         lat = latitude,
         lon = longitude
     )

     if (cityWeatherLocalDataSource.isEmpty()) {

         cityWeatherLocalDataSource.addCity(cityServer)

     } else {

         cityWeatherLocalDataSource.makeAllCitiesAsNotJustAdded()
         val city = cityWeatherLocalDataSource.loadCity(cityServer.name)
         city.fold({
             // In case of error does nothing

         }) { cityInfo ->

             if (cityInfo.name.isNotEmpty())

                 cityWeatherLocalDataSource.updateCity(
                     cityName = cityInfo.name,
                     weatherDescription = cityInfo.weatherDescription,
                     weatherIcon = cityInfo.weatherIcon,
                     pressure = cityInfo.pressure,
                     temperatureMax = cityInfo.temperatureMax,
                     temperatureMin = cityInfo.temperatureMin,
                     temperature = cityInfo.temperature
                 )
             else
                 cityWeatherLocalDataSource.addCity(cityServer)
         }

     }

 }

}

 */