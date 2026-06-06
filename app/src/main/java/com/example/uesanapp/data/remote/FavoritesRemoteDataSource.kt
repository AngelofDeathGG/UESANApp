package com.example.uesanapp.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FavoritesRemoteDataSource {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private fun favoritesCollection(userId: String) =
        firestore.collection("user_favorites")
            .document(userId)
            .collection("favorites")

    suspend fun addFavorite(userId: String, countryId: String, countryName: String) {
        val data = hashMapOf(
            "countryName" to countryName,
            "createdAt" to System.currentTimeMillis()
        )
        favoritesCollection(userId)
            .document(countryId)
            .set(data)
            .await()
    }

    suspend fun removeFavorite(userId: String, countryId: String) {
        favoritesCollection(userId)
            .document(countryId)
            .delete()
            .await()
    }
}
