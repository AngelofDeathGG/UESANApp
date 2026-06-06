package com.example.uesanapp.data.local

import androidx.room.Entity

@Entity(
    tableName = "favorites",
    primaryKeys = ["userId", "countryId"]
)
data class FavoriteEntity(
    val userId: String,
    val countryId: String,
    val countryName: String,
    val createdAt: Long = System.currentTimeMillis()
)
