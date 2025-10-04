package com.votewise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_questions")
data class QuizQuestion(
    @PrimaryKey val id: String,
    val category: String,
    val question: String,
    val options: List<String>,
    val weight: Double = 1.0,
    val isActive: Boolean = true,
    val createdAt: String
)