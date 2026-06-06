package com.example.uesanapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {

    @Query("SELECT * FROM countries ORDER BY favoriteCount DESC, fifaRanking ASC")
    fun observeAll(): Flow<List<CountryEntity>>

    @Query("SELECT * FROM countries WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): CountryEntity?

    @Query("SELECT COUNT(*) FROM countries")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(countries: List<CountryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(country: CountryEntity)

    @Query("UPDATE countries SET favoriteCount = favoriteCount + 1 WHERE id = :id")
    suspend fun incrementFavoriteCount(id: String)

    @Query("UPDATE countries SET favoriteCount = favoriteCount - 1 WHERE id = :id")
    suspend fun decrementFavoriteCount(id: String)
}
