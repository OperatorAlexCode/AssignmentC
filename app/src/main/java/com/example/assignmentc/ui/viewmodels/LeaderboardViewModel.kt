package com.example.assignmentc.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.assignmentc.data.ScoreEntry
import com.example.assignmentc.data.ScoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LeaderboardViewModel(
    private val repository: ScoreRepository
) : ViewModel() {
    val localScores = repository.allScores

    private val _onlineScores = MutableStateFlow<List<ScoreEntry>>(emptyList())
    val onlineScores = _onlineScores.asStateFlow()

    fun submitLocalScore(name: String, score: Int) {
        viewModelScope.launch {
            repository.submitScore(name, score)
        }
    }

    // Add your online leaderboard implementation here
    fun loadOnlineScores() {
        // Implement your online leaderboard logic
    }
}

class LeaderboardViewModelFactory(private val repository: ScoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeaderboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LeaderboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}