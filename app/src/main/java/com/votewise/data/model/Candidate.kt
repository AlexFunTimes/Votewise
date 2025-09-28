package com.votewise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "candidates")
data class Candidate(
    @PrimaryKey val id: String,
    val name: String,
    val party: String,
    val imageUrl: String?,
    val office: String?,
    val district: String?,
    val electionYear: Int?,
    val status: CandidateStatus?,
    val electionType: ElectionType?,
    val consistencyScore: Double?
)

enum class CandidateStatus {
    DECLARED,
    EXPLORATORY,
    QUALIFIED,
    WITHDRAWN,
    DEFEATED,
    ELECTED
}