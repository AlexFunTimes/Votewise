package com.votewise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.votewise.data.converters.MapConverter

@Entity(tableName = "candidate_matches")
@TypeConverters(MapConverter::class)
data class CandidateMatch(
    @PrimaryKey val id: String,
    val userId: String,
    val candidateId: String,
    val matchPercentage: Double,
    val categoryMatches: Map<String, Double>,
    val quizSessionId: String,
    val createdAt: String,
    val matchScore: Double
)