package com.example.uesanapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.uesanapp.UESANApp
import com.example.uesanapp.data.auth.CurrentUserProvider
import com.example.uesanapp.data.local.CountryEntity
import com.example.uesanapp.data.model.CountryModel
import com.example.uesanapp.data.repository.CountryRepository
import com.example.uesanapp.data.repository.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val countryRepository: CountryRepository,
    private val favoritesRepository: FavoritesRepository,
    private val currentUserProvider: CurrentUserProvider
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val countries: StateFlow<List<CountryModel>> = flowOf(currentUserProvider.getUserId())
        .flatMapLatest { uid ->
            val countryFlow = countryRepository.observeCountries()
            if (uid == null) {
                countryFlow.map { entities ->
                    entities.map { it.toModel(isFavorite = false) }
                }
            } else {
                combine(
                    countryFlow,
                    favoritesRepository.observeFavoriteCountryIds(uid)
                ) { entities, favoriteIds ->
                    val favSet = favoriteIds.toSet()
                    entities.map { it.toModel(isFavorite = it.id in favSet) }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            countryRepository.seedIfEmpty()
            _isLoading.value = false
        }
    }

    fun toggleFavorite(countryId: String) {
        val uid = currentUserProvider.getUserId() ?: return
        val country = countries.value.firstOrNull { it.id == countryId } ?: return
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(uid, countryId, country.name)
        }
    }

    private fun CountryEntity.toModel(isFavorite: Boolean) = CountryModel(
        id = id,
        name = name,
        ranking = fifaRanking,
        imageUrl = imageUrl,
        favoriteCount = favoriteCount,
        isFavorite = isFavorite
    )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as UESANApp
                HomeViewModel(
                    countryRepository = application.container.countryRepository,
                    favoritesRepository = application.container.favoritesRepository,
                    currentUserProvider = application.container.currentUserProvider
                )
            }
        }
    }
}
