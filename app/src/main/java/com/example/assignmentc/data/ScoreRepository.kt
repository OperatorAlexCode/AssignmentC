package com.example.assignmentc.data

import kotlinx.coroutines.flow.Flow

class ScoreRepository(private val scoreDao: ScoreDao) {
    val allScores: Flow<List<ScoreEntry>> = scoreDao.getAllScores()

    suspend fun submitScore(name: String, score: Int) {
        val existingScore = scoreDao.getScoreByUsername(name)
        if (existingScore == null || score > existingScore.score) {
            scoreDao.insertScore(ScoreEntry(name = name, score = score))
        }
    }
}