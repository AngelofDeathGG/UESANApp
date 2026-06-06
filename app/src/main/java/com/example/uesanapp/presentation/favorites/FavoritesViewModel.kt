package com.example.uesanapp.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.uesanapp.UESANApp
import com.example.uesanapp.data.auth.CurrentUserProvider
import com.example.uesanapp.data.model.CountryModel
import com.example.uesanapp.data.repository.FavoritesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository,
    private val currentUserProvider: CurrentUserProvider
) : ViewModel() {

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val favorites: StateFlow<List<CountryModel>> = flowOf(currentUserProvider.getUserId())
        .flatMapLatest { uid ->
            if (uid == null) {
                flowOf(emptyList())
            } else {
                favoritesRepository.observeFavoriteCountries(uid)
                    .map { entities ->
                        entities.map {
                            CountryModel(
                                id = it.id,
                                name = it.name,
                                ranking = it.fifaRanking,
                                imageUrl = it.imageUrl,
                                favoriteCount = it.favoriteCount,
                                isFavorite = true
                            )
                        }
                    }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleFavorite(countryId: String) {
        val uid = currentUserProvider.getUserId() ?: return
        val country = favorites.value.firstOrNull { it.id == countryId } ?: return
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(uid, countryId, country.name)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as UESANApp
                FavoritesViewModel(
                    favoritesRepository = application.container.favoritesRepository,
                    currentUserProvider = application.container.currentUserProvider
                )
            }
        }
    }
}
