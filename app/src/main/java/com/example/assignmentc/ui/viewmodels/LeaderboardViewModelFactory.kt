package com.example.assignmentc.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.assignmentc.data.AppDatabase
import com.example.assignmentc.data.FirestoreRepository
import com.example.assignmentc.data.ScoreRepository

class LeaderboardViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeaderboardViewModel::class.java)) {
            // Create database and DAO
            val database = AppDatabase.getDatabase(context)
            val scoreDao = database.scoreDao()

            // Create repositories
            val scoreRepository = ScoreRepository(scoreDao)
            val firestoreRepository = FirestoreRepository()

            return LeaderboardViewModel(scoreRepository, firestoreRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}