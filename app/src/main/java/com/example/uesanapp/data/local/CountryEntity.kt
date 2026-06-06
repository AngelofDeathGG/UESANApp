package com.example.uesanapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val imageUrl: String,
    val fifaRanking: Int,
    val favoriteCount: Int = 0
)
