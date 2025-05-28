package com.example.assignmentc.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignmentc.data.FirestoreRepository
import com.example.assignmentc.data.ScoreEntry
import com.example.assignmentc.data.ScoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LeaderboardViewModel(
    private val repository: ScoreRepository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {
    val localScores = repository.allScores

    private val _onlineScores = MutableStateFlow<List<ScoreEntry>>(emptyList())
    val onlineScores = _onlineScores.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    fun clearError() { _errorMessage.value = "" }

    fun loadOnlineScores() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _onlineScores.value = firestoreRepository.getAllScores()
            } catch (e: Exception) {
                _errorMessage.value = "Refresh failed: ${e.message}"
                Log.e("Leaderboard", "Refresh failed", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun submitLocalScore(name: String, score: Int) {
        viewModelScope.launch {
            repository.submitScore(name, score)
        }
    }

    fun submitOnlineScore(name: String, score: Int) {
        viewModelScope.launch {
            try {
                firestoreRepository.submitScore(name, score)
                // Refresh scores after successful submission
                loadOnlineScores()
            } catch (e: Exception) {
                _errorMessage.value = "Submit failed: ${e.message}"
                Log.e("Leaderboard", "Submit failed", e)
            }
        }
    }
}