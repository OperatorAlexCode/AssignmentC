package com.example.assignmentc.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()
    private val scoresCollection = db.collection("scores")

    suspend fun submitScore(name: String, score: Int) {
        val query = scoresCollection.whereEqualTo("username", name).get().await()

        if (query.documents.isEmpty()) {
            // New user
            scoresCollection.add(mapOf(
                "username" to name,
                "score" to score
            )).await()
        } else {
            // Update existing score if higher
            val existingScore = query.documents.first().getLong("score")?.toInt() ?: 0
            if (score > existingScore) {
                query.documents.first().reference.update("score", score).await()
            }
        }
    }

    suspend fun getAllScores(): List<ScoreEntry> {
        return scoresCollection
            .orderBy("score", Query.Direction.DESCENDING)
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                ScoreEntry(
                    id = doc.id.hashCode().toLong(),
                    name = doc.getString("username") ?: "",
                    score = doc.getLong("score")?.toInt() ?: 0
                )
            }
    }
}