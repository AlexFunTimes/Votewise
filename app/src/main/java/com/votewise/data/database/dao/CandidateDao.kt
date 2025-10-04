package com.votewise.data.database.dao

import androidx.room.*
import com.votewise.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDao {
    @Query("SELECT * FROM candidates WHERE isActive = 1")
    fun getAllCandidates(): Flow<List<Candidate>>

    @Query("SELECT * FROM candidates WHERE id = :candidateId")
    suspend fun getCandidateById(candidateId: String): Candidate?

    @Query("SELECT * FROM candidates WHERE state = :state AND electionType = :electionType")
    fun getCandidatesByStateAndType(state: String, electionType: ElectionType): Flow<List<Candidate>>

    @Query("SELECT * FROM candidates WHERE district = :district AND state = :state")
    fun getCandidatesByDistrict(district: String, state: String): Flow<List<Candidate>>

    @Query("SELECT * FROM candidates WHERE party = :party")
    fun getCandidatesByParty(party: String): Flow<List<Candidate>>

    @Query("SELECT * FROM candidates WHERE name LIKE '%' || :query || '%' OR office LIKE '%' || :query || '%'")
    fun searchCandidates(query: String): Flow<List<Candidate>>

    @Query("SELECT * FROM candidates WHERE electionYear = :year")
    fun getCandidatesByElectionYear(year: Int): Flow<List<Candidate>>

    @Query("SELECT * FROM candidates WHERE isIncumbent = 1")
    fun getIncumbentCandidates(): Flow<List<Candidate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCandidate(candidate: Candidate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCandidates(candidates: List<Candidate>)

    @Update
    suspend fun updateCandidate(candidate: Candidate)

    @Delete
    suspend fun deleteCandidate(candidate: Candidate)

    @Query("UPDATE candidates SET consistencyScore = :score WHERE id = :candidateId")
    suspend fun updateConsistencyScore(candidateId: String, score: Double)

    // Candidate Positions
    @Query("SELECT * FROM candidate_positions WHERE candidateId = :candidateId ORDER BY date DESC")
    fun getCandidatePositions(candidateId: String): Flow<List<CandidatePosition>>

    @Query("SELECT * FROM candidate_positions WHERE candidateId = :candidateId AND issue = :issue")
    fun getCandidatePositionByIssue(candidateId: String, issue: String): Flow<List<CandidatePosition>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCandidatePosition(position: CandidatePosition)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCandidatePositions(positions: List<CandidatePosition>)

    // Voting Records
    @Query("SELECT * FROM candidate_voting_records WHERE candidateId = :candidateId ORDER BY date DESC")
    fun getVotingRecords(candidateId: String): Flow<List<VotingRecord>>

    @Query("SELECT * FROM candidate_voting_records WHERE candidateId = :candidateId AND vote = :voteType")
    fun getVotingRecordsByType(candidateId: String, voteType: VoteType): Flow<List<VotingRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVotingRecord(record: VotingRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVotingRecords(records: List<VotingRecord>)

    // Candidate News
    @Query("SELECT * FROM candidate_news WHERE candidateId = :candidateId ORDER BY publishedAt DESC")
    fun getCandidateNews(candidateId: String): Flow<List<CandidateNews>>

    @Query("SELECT * FROM candidate_news WHERE candidateId = :candidateId AND isPositive = :isPositive")
    fun getCandidateNewsBySentiment(candidateId: String, isPositive: Boolean): Flow<List<CandidateNews>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCandidateNews(news: CandidateNews)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCandidateNewsList(newsList: List<CandidateNews>)

    @Query("DELETE FROM candidate_news WHERE publishedAt < :cutoffDate")
    suspend fun deleteOldNews(cutoffDate: java.util.Date)
}


