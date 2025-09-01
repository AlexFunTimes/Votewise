package com.votewise.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.votewise.data.model.Candidate
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDao {
    @Query("SELECT * FROM candidates WHERE electionYear = :electionYear")
    fun getCandidatesForElection(electionYear: Int): Flow<List<Candidate>>

    @Query("SELECT * FROM candidates") // New method to get all candidates
    fun getAllCandidates(): Flow<List<Candidate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(candidates: List<Candidate>)
}
