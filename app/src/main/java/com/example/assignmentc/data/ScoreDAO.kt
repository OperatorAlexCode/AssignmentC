package com.example.assignmentc.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(scoreEntry: ScoreEntry)

    @Query("SELECT * FROM scores ORDER BY score DESC")
    fun getAllScores(): Flow<List<ScoreEntry>>

    @Query("SELECT * FROM scores WHERE name = :username LIMIT 1")
    suspend fun getScoreByUsername(username: String): ScoreEntry?
}