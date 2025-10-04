package com.votewise.data.database.dao

import androidx.room.*
import com.votewise.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    // Quiz Questions
    @Query("SELECT * FROM quiz_questions")
    fun getAllActiveQuestions(): Flow<List<QuizQuestion>>

    @Query("SELECT * FROM quiz_questions WHERE category = :category")
    fun getQuestionsByCategory(category: String): Flow<List<QuizQuestion>>

    @Query("SELECT * FROM quiz_questions WHERE id = :questionId")
    suspend fun getQuestionById(questionId: String): QuizQuestion?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuizQuestion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuizQuestion>)

    @Update
    suspend fun updateQuestion(question: QuizQuestion)

    @Delete
    suspend fun deleteQuestion(question: QuizQuestion)

    // Quiz Sessions
    @Query("SELECT * FROM quiz_sessions WHERE userId = :userId ORDER BY startTime DESC")
    fun getUserQuizSessions(userId: String): Flow<List<QuizSession>>

    @Query( "SELECT * FROM quiz_sessions WHERE id = :sessionId")
    suspend fun getQuizSession(sessionId: String): QuizSession?

    @Query("SELECT * FROM quiz_sessions WHERE userId = :userId ORDER BY startTime DESC LIMIT 1")
    suspend fun getLatestCompletedSession(userId: String): QuizSession?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizSession(session: QuizSession)

    @Update
    suspend fun updateQuizSession(session: QuizSession)

    @Query("UPDATE quiz_sessions SET isComplete = 1, completedAt = :completedAt WHERE id = :sessionId")
    suspend fun markSessionComplete(sessionId: String, completedAt: String)

    // Quiz Responses
    @Query("SELECT * FROM quiz_responses WHERE quizSessionId = :sessionId")
    fun getSessionResponses(sessionId: String): Flow<List<QuizResponse>>

    @Query("SELECT * FROM quiz_responses WHERE questionId = :questionId")
    fun getUserResponseToQuestion(questionId: String): Flow<List<QuizResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponse(response: QuizResponse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponses(responses: List<QuizResponse>)

    @Update
    suspend fun updateResponse(response: QuizResponse)

    @Delete
    suspend fun deleteResponse(response: QuizResponse)

    // Candidate Matches
    @Query("SELECT * FROM candidate_matches WHERE userId = :userId ORDER BY matchScore DESC")
    fun getUserMatches(userId: String): Flow<List<CandidateMatch>>

    @Query("SELECT * FROM candidate_matches WHERE userId = :userId AND candidateId = :candidateId")
    suspend fun getMatchForCandidate(userId: String, candidateId: String): CandidateMatch?

    @Query("SELECT * FROM candidate_matches WHERE userId = :userId AND matchScore >= :minPercentage ORDER BY matchScore DESC")
    fun getMatchesAboveThreshold(userId: String, minPercentage: Double): Flow<List<CandidateMatch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: CandidateMatch)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(matches: List<CandidateMatch>)

    @Update
    suspend fun updateMatch(match: CandidateMatch)

    @Delete
    suspend fun deleteMatch(match: CandidateMatch)

    @Query("DELETE FROM candidate_matches WHERE userId = :userId")
    suspend fun deleteUserMatches(userId: String)

    // Complex queries for quiz results
    @Query("""
        SELECT qr.questionId, qr.selectedOption, qq.category
        FROM quiz_responses qr
        JOIN quiz_questions qq ON qr.questionId = qq.id
        WHERE qr.quizSessionId = :sessionId
    """)
    suspend fun getSessionResponseDetails(sessionId: String): List<QuizResponseDetail>

    @Query("""
        SELECT category, COUNT(*) as responseCount
        FROM quiz_responses qr
        JOIN quiz_questions qq ON qr.questionId = qq.id
        WHERE qr.quizSessionId = :sessionId
        GROUP BY category
    """)
    suspend fun getCategoryScores(sessionId: String): List<CategoryScore>
}

data class QuizResponseDetail(
    val questionId: String,
    val selectedOption: String,
    val category: String
)

data class CategoryScore(
    val category: String,
    val responseCount: Int
)
