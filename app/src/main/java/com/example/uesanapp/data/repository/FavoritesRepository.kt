package com.example.uesanapp.data.repository

import android.util.Log
import com.example.uesanapp.data.local.CountryDao
import com.example.uesanapp.data.local.CountryEntity
import com.example.uesanapp.data.local.FavoriteDao
import com.example.uesanapp.data.local.FavoriteEntity
import com.example.uesanapp.data.remote.FavoritesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class FavoritesRepository(
    private val favoriteDao: FavoriteDao,
    private val countryDao: CountryDao,
    private val remoteDataSource: FavoritesRemoteDataSource
) {

    fun observeFavoriteCountryIds(userId: String): Flow<List<String>> =
        favoriteDao.observeCountryIdsByUser(userId)

    fun observeFavoriteCountries(userId: String): Flow<List<CountryEntity>> =
        combine(
            favoriteDao.observeByUser(userId),
            countryDao.observeAll()
        ) { favorites, countries ->
            val favoriteIds = favorites.map { it.countryId }.toSet()
            countries
                .filter { it.id in favoriteIds }
                .sortedByDescending { it.favoriteCount }
        }

    suspend fun isFavorite(userId: String, countryId: String): Boolean =
        favoriteDao.isFavorite(userId, countryId)

    suspend fun toggleFavorite(userId: String, countryId: String, countryName: String) {
        val isFav = favoriteDao.isFavorite(userId, countryId)

        if (isFav) {
            favoriteDao.deleteByIds(userId, countryId)
            countryDao.decrementFavoriteCount(countryId)
        } else {
            favoriteDao.insert(
                FavoriteEntity(
                    userId = userId,
                    countryId = countryId,
                    countryName = countryName
                )
            )
            countryDao.incrementFavoriteCount(countryId)
        }

        runCatching {
            if (isFav) {
                remoteDataSource.removeFavorite(userId, countryId)
            } else {
                remoteDataSource.addFavorite(userId, countryId, countryName)
            }
        }.onFailure { e ->
            Log.w(TAG, "Firestore sync failed (using local only): ${e.message}")
        }
    }

    private companion object {
        const val TAG = "FavoritesRepository"
    }
}
