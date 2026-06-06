package com.example.uesanapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorites WHERE userId = :userId")
    fun observeByUser(userId: String): Flow<List<FavoriteEntity>>

    @Query("SELECT countryId FROM favorites WHERE userId = :userId")
    fun observeCountryIdsByUser(userId: String): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND countryId = :countryId)")
    suspend fun isFavorite(userId: String, countryId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Delete
    suspend fun delete(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE userId = :userId AND countryId = :countryId")
    suspend fun deleteByIds(userId: String, countryId: String)
}
