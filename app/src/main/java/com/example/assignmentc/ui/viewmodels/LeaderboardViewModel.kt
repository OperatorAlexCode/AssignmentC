package com.example.assignmentc.ui.viewmodels

import androidx.lifecycle.ViewModel
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

    fun loadOnlineScores() {
        // TODO: online leaderboard if there's time
    }
}