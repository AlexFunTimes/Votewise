package com.votewise.data.database.dao

import androidx.room.*
import com.votewise.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectionDao {
    @Query("SELECT * FROM elections WHERE status = :status ORDER BY electionDate ASC")
    fun getElectionsByStatus(status: String): Flow<List<Election>>

    @Query("SELECT * FROM elections WHERE state = :state ORDER BY electionDate ASC")
    fun getElectionsByState(state: String): Flow<List<Election>>

    @Query("SELECT * FROM elections WHERE electionDate >= :startDate AND electionDate <= :endDate ORDER BY electionDate ASC")
    fun getElectionsInDateRange(startDate: String, endDate: String): Flow<List<Election>>

    @Query("SELECT * FROM elections WHERE id = :electionId")
    suspend fun getElectionById(electionId: String): Election?

    @Query("SELECT * FROM elections WHERE electionDate >= :date ORDER BY electionDate ASC LIMIT :limit")
    fun getUpcomingElections(date: String, limit: Int = 10): Flow<List<Election>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElection(election: Election)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElections(elections: List<Election>)

    @Update
    suspend fun updateElection(election: Election)

    @Delete
    suspend fun deleteElection(election: Election)

    // Election Candidates
    @Query("SELECT * FROM election_candidates WHERE electionId = :electionId")
    fun getElectionCandidates(electionId: String): Flow<List<ElectionCandidate>>

    @Query("SELECT * FROM election_candidates WHERE candidateId = :candidateId")
    fun getElectionsForCandidate(candidateId: String): Flow<List<ElectionCandidate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElectionCandidate(electionCandidate: ElectionCandidate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElectionCandidates(electionCandidates: List<ElectionCandidate>)

    // Voter Registration
    @Query("SELECT * FROM voter_registration WHERE userId = :userId")
    suspend fun getUserRegistration(userId: String): VoterRegistration?

    @Query("SELECT * FROM voter_registration WHERE state = :state")
    fun getRegistrationsByState(state: String): Flow<List<VoterRegistration>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVoterRegistration(registration: VoterRegistration)

    @Update
    suspend fun updateVoterRegistration(registration: VoterRegistration)

    @Query("UPDATE voter_registration SET registrationStatus = :status WHERE userId = :userId")
    suspend fun updateRegistrationStatus(userId: String, status: String)
}
