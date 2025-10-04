package com.votewise.data.repository

import com.votewise.data.database.dao.QuizDao
import com.votewise.data.database.dao.CandidateDao
import com.votewise.data.model.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
    private val quizDao: QuizDao,
    private val candidateDao: CandidateDao
) {
    fun getAllQuestions(): Flow<List<QuizQuestion>> = quizDao.getAllActiveQuestions()

    fun getQuestionsByCategory(category: String): Flow<List<QuizQuestion>> =
        quizDao.getQuestionsByCategory(category)

    suspend fun getQuestionById(questionId: String): QuizQuestion? =
        quizDao.getQuestionById(questionId)

    suspend fun insertQuestion(question: QuizQuestion) =
        quizDao.insertQuestion(question)

    suspend fun insertQuestions(questions: List<QuizQuestion>) =
        quizDao.insertQuestions(questions)

    suspend fun createQuizSession(userId: String): QuizSession {
        val session = QuizSession(
            id = generateSessionId(),
            userId = userId,
            totalQuestions = 0,
            answeredQuestions = 0,
            isComplete = false,
            createdAt = java.util.Date().toString(),
            startTime = java.util.Date().toString()
        )
        quizDao.insertQuizSession(session)
        return session
    }

    suspend fun updateQuizSession(session: QuizSession) =
        quizDao.updateQuizSession(session)

    suspend fun completeQuizSession(sessionId: String) {
        quizDao.markSessionComplete(sessionId, java.util.Date().toString())
    }

    suspend fun saveResponse(response: QuizResponse) =
        quizDao.insertResponse(response)

    suspend fun saveResponses(responses: List<QuizResponse>) =
        quizDao.insertResponses(responses)

    fun getSessionResponses(sessionId: String): Flow<List<QuizResponse>> =
        quizDao.getSessionResponses(sessionId)

    suspend fun calculateMatches(userId: String, sessionId: String): List<CandidateMatch> {
        val responses = quizDao.getSessionResponseDetails(sessionId)
        val categoryScores = quizDao.getCategoryScores(sessionId)

        // Get all candidates for matching
        val candidates = candidateDao.getAllCandidates()

        val matches = mutableListOf<CandidateMatch>()

        // Calculate match percentages for each candidate
        candidates.collect { candidateList ->
            candidateList.forEach { candidate ->
                val matchPercentage = calculateMatchPercentage(responses, categoryScores, candidate)
                if (matchPercentage > 0) {
                    val match = CandidateMatch(
                        id = generateMatchId(),
                        userId = userId,
                        candidateId = candidate.id,
                        matchPercentage = matchPercentage,
                        categoryMatches = calculateCategoryMatches(responses, categoryScores, candidate),
                        quizSessionId = sessionId,
                        createdAt = java.util.Date().toString(),
                        matchScore = matchPercentage
                    )
                    matches.add(match)
                }
            }
        }

        // Save matches to database
        quizDao.insertMatches(matches)

        return matches.sortedByDescending { it.matchPercentage }
    }

    fun getUserMatches(userId: String): Flow<List<CandidateMatch>> =
        quizDao.getUserMatches(userId)

    fun getMatchesAboveThreshold(userId: String, minPercentage: Double): Flow<List<CandidateMatch>> =
        quizDao.getMatchesAboveThreshold(userId, minPercentage)

    suspend fun getLatestCompletedSession(userId: String): QuizSession? =
        quizDao.getLatestCompletedSession(userId)

    private fun calculateMatchPercentage(
        responses: List<com.votewise.data.database.dao.QuizResponseDetail>,
        categoryScores: List<com.votewise.data.database.dao.CategoryScore>,
        candidate: Candidate
    ): Double {
        // This is a simplified matching algorithm
        // In a real implementation, you would compare user responses with candidate positions
        var totalScore = 0.0
        var totalWeight = 0.0

        responses.forEach { response ->
            // Get candidate position for this question's category
            val candidatePosition = getCandidatePositionForCategory(candidate, response.category)
            if (candidatePosition != null) {
                val userScore = getScoreForOption(response.selectedOption)
                val candidateScore = getScoreForPosition(candidatePosition)
                val difference = 1.0 - kotlin.math.abs(userScore - candidateScore) / 10.0
                totalScore += difference * 1.0 // Use default weight since response.weight is not available
                totalWeight += 1.0
            }
        }

        return if (totalWeight > 0) (totalScore / totalWeight) * 100 else 0.0
    }

    private fun calculateCategoryMatches(
        responses: List<com.votewise.data.database.dao.QuizResponseDetail>,
        categoryScores: List<com.votewise.data.database.dao.CategoryScore>,
        candidate: Candidate
    ): Map<String, Double> {
        val categoryMatches = mutableMapOf<String, Double>()

        categoryScores.forEach { categoryScore ->
            val categoryResponses = responses.filter { it.category == categoryScore.category }
            if (categoryResponses.isNotEmpty()) {
                val categoryMatch = calculateMatchPercentage(categoryResponses, listOf(categoryScore), candidate)
                categoryMatches[categoryScore.category] = categoryMatch
            }
        }

        return categoryMatches
    }

    private fun getCandidatePositionForCategory(candidate: Candidate, category: String): String? {
        // This would typically query the candidate positions from the database
        // For now, return a placeholder
        return when (category.lowercase()) {
            "economy" -> "Pro-business"
            "healthcare" -> "Universal healthcare"
            "environment" -> "Green energy"
            "education" -> "Public education"
            "immigration" -> "Comprehensive reform"
            else -> null
        }
    }

    private fun getScoreForOption(option: String): Double {
        // Convert user's selected option to a numerical score
        return when (option.lowercase()) {
            "strongly agree" -> 9.0
            "agree" -> 7.0
            "somewhat agree" -> 5.0
            "neutral" -> 5.0
            "somewhat disagree" -> 3.0
            "disagree" -> 1.0
            "strongly disagree" -> 0.0
            else -> 5.0
        }
    }

    private fun getScoreForPosition(position: String): Double {
        // Convert candidate position to a numerical score
        return when (position.lowercase()) {
            "very liberal" -> 9.0
            "liberal" -> 7.0
            "moderate" -> 5.0
            "conservative" -> 3.0
            "very conservative" -> 1.0
            else -> 5.0
        }
    }

    private fun generateSessionId(): String = "session_${System.currentTimeMillis()}_${(1000..9999).random()}"
    private fun generateMatchId(): String = "match_${System.currentTimeMillis()}_${(1000..9999).random()}"
}