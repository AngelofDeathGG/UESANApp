package com.example.uesanapp.data.model

data class CountryModel(
    val id: String,
    val name: String,
    val ranking: Int,
    val imageUrl: String,
    val favoriteCount: Int = 0,
    val isFavorite: Boolean = false
)
