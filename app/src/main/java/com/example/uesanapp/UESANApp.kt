package com.example.uesanapp

import android.app.Application
import com.example.uesanapp.data.auth.CurrentUserProvider
import com.example.uesanapp.data.local.AppDatabase
import com.example.uesanapp.data.remote.FavoritesRemoteDataSource
import com.example.uesanapp.data.repository.CountryRepository
import com.example.uesanapp.data.repository.FavoritesRepository

class UESANApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

class AppContainer(application: Application) {
    val currentUserProvider = CurrentUserProvider(application)
    private val database = AppDatabase.getInstance(application)
    private val favoritesRemoteDataSource = FavoritesRemoteDataSource()

    val countryRepository = CountryRepository(database.countryDao())
    val favoritesRepository = FavoritesRepository(
        favoriteDao = database.favoriteDao(),
        countryDao = database.countryDao(),
        remoteDataSource = favoritesRemoteDataSource
    )
}
