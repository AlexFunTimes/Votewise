package com.votewise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_responses")
data class QuizResponse(
    @PrimaryKey val id: String,
    val userId: String,
    val questionId: String,
    val selectedOption: String,
    val confidence: Int = 5,
    val answeredAt: String
)