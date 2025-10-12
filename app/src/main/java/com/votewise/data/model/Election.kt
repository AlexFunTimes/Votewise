package com.votewise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "elections")
data class Election(
    @PrimaryKey val id: String,
    val name: String,
    val electionDay: String?, // Google Civic API format
    val ocdDivisionId: String? // Google Civic API format
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
