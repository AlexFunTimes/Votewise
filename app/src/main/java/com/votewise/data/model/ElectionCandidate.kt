package com.votewise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "election_candidates")
data class ElectionCandidate(
    @PrimaryKey val id: String,
    val electionId: String,
    val candidateId: String,
    val ballotOrder: Int?,
    val isWriteIn: Boolean = false,
    val isQualified: Boolean = true
)
