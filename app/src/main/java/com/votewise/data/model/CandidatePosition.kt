package com.votewise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "candidate_positions")
data class CandidatePosition(
    @PrimaryKey val id: String,
    val candidateId: String,
    val issue: String,
    val position: String,
    val source: String,
    val sourceUrl: String?,
    val date: String,
    val confidence: Double = 1.0
)
