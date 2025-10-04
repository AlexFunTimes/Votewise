package com.votewise.data.model

data class QuizCategory(
    val name: String,
    val displayName: String,
    val description: String,
    val weight: Double = 1.0,
    val icon: String? = null
)

data class QuizResult(
    val sessionId: String,
    val totalQuestions: Int,
    val answeredQuestions: Int,
    val completionPercentage: Double,
    val categoryScores: Map<String, Double>,
    val topMatches: List<CandidateMatch>,
    val completedAt: String
)
