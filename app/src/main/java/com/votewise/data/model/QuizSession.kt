package com.votewise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_sessions")
data class QuizSession(
    @PrimaryKey val id: String,
    val userId: String,
    val completedAt: String? = null,
    val isComplete: Boolean = false,
    val totalQuestions: Int = 0,
    val answeredQuestions: Int = 0,
    val createdAt: String,
    val quizType: String = "POLITICAL",
    val startTime: String,
    val endTime: String? = null,
    val score: Double? = null
)