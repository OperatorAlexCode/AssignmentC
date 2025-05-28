package com.example.assignmentc.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()
    private val scoresCollection = db.collection("scores")

    // Use constants for consistent field names
    private companion object {
        const val FIELD_NAME = "name"
        const val FIELD_SCORE = "score"
    }

    suspend fun submitScore(name: String, score: Int) {
        try {
            val query = scoresCollection
                .whereEqualTo(FIELD_NAME, name)
                .get()
                .await()

            if (query.isEmpty) {
                // New user - add document
                scoresCollection.add(mapOf(
                    FIELD_NAME to name,
                    FIELD_SCORE to score
                )).await()
                Log.d("Firestore", "Added new score for $name")
            } else {
                // Update existing user's score if higher
                val document = query.documents.first()
                val existingScore = document.getLong(FIELD_SCORE)?.toInt() ?: 0

                if (score > existingScore) {
                    document.reference.update(FIELD_SCORE, score).await()
                    Log.d("Firestore", "Updated score for $name")
                } else {
                    Log.d("Firestore", "Score not updated (new score $score <= existing $existingScore)")
                }
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Submit failed: ${e.message}")
            throw e // Rethrow for ViewModel handling
        }
    }

    suspend fun getAllScores(): List<ScoreEntry> {
        return try {
            scoresCollection
                .orderBy(FIELD_SCORE, Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { doc ->
                    ScoreEntry(
                        id = doc.id.hashCode().toLong(),
                        name = doc.getString(FIELD_NAME) ?: "",
                        score = doc.getLong(FIELD_SCORE)?.toInt() ?: 0
                    )
                }.also {
                    Log.d("Firestore", "Fetched ${it.size} scores")
                }
        } catch (e: Exception) {
            Log.e("Firestore", "Fetch failed: ${e.message}")
            emptyList()
        }
    }
}