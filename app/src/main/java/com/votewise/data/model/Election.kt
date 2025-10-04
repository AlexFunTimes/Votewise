package com.votewise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "elections")
data class Election(
    @PrimaryKey val id: String,
    val name: String,
    val type: String,
    val state: String?,
    val district: String?,
    val county: String?,
    val electionDate: String, // Changed from Date to String
    val registrationDeadline: String?, // Changed from Date to String
    val earlyVotingStart: String?, // Changed from Date to String
    val earlyVotingEnd: String?, // Changed from Date to String
    val absenteeDeadline: String?, // Changed from Date to String
    val isPrimary: Boolean = false,
    val isGeneral: Boolean = false,
    val isSpecial: Boolean = false,
    val status: String,
    val description: String?,
    val website: String?
)

enum class ElectionStatus {
    UPCOMING,
    ACTIVE,
    COMPLETED,
    CANCELLED
}

enum class RegistrationStatus {
    REGISTERED,
    NOT_REGISTERED,
    PENDING,
    INELIGIBLE,
    UNKNOWN
}
