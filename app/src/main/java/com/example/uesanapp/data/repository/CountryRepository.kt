package com.example.uesanapp.data.repository

import com.example.uesanapp.data.local.CountryDao
import com.example.uesanapp.data.local.CountryEntity
import kotlinx.coroutines.flow.Flow

class CountryRepository(private val countryDao: CountryDao) {

    fun observeCountries(): Flow<List<CountryEntity>> = countryDao.observeAll()

    suspend fun seedIfEmpty() {
        if (countryDao.count() > 0) return
        countryDao.upsertAll(seedCountries)
    }

    companion object {
        private val seedCountries = listOf(
            CountryEntity("co", "Colombia", "https://flagcdn.com/w320/co.png", 5),
            CountryEntity("fr", "Francia", "https://flagcdn.com/w320/fr.png", 3),
            CountryEntity("br", "Brasil", "https://flagcdn.com/w320/br.png", 8),
            CountryEntity("es", "España", "https://flagcdn.com/w320/es.png", 2),
            CountryEntity("pt", "Portugal", "https://flagcdn.com/w320/pt.png", 7),
            CountryEntity("ar", "Argentina", "https://flagcdn.com/w320/ar.png", 1),
            CountryEntity("jp", "Japón", "https://flagcdn.com/w320/jp.png", 10),
            CountryEntity("pe", "Perú", "https://flagcdn.com/w320/pe.png", 50)
        )
    }
}
