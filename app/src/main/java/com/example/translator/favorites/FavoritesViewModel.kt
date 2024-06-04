package com.example.translator.favorites

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


sealed class FavoritesState {
    object Loading : FavoritesState()
    data class Success(val state: FavoriteTranslations?) : FavoritesState()
}

class FavoritesViewModel(
    private val repository: FavoritesRepository = FavoritesRepository()
) : ViewModel() {
    init {
        viewModelScope.launch {
            getFavorites()
        }
    }

    val state = MutableStateFlow<FavoritesState>(FavoritesState.Loading)

    fun SaveToFavorites(item: String, context: Context) {
        viewModelScope.launch {
            try {
                repository.saveTextToFirestore(item, context)
                // Get updated favorites from Firestore
                val updatedFavorites = repository.getFavorites()
                state.value = FavoritesState.Success(updatedFavorites)
            } catch (e: Exception) {
                // Handle the error
            }
        }
    }
    private suspend fun getFavorites() {
        val result = repository.getFavorites()
        state.value = FavoritesState.Success(result)
    }
    fun deleteFavorite(item: String, context: Context) {
        viewModelScope.launch {
            try {
                repository.deleteFavoriteFromFirestore(item, context)
                // Get updated favorites from Firestore
                val updatedFavorites = repository.getFavorites()
                state.value = FavoritesState.Success(updatedFavorites)
            } catch (e: Exception) {
                // Handle the error
            }
        }
    }
}