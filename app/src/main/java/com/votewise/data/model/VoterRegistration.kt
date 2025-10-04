package com.votewise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "voter_registration")
data class VoterRegistration(
    @PrimaryKey val id: String,
    val userId: String,
    val state: String,
    val registrationStatus: String,
    val registrationDate: String?,
    val lastChecked: String,
    val registrationUrl: String?
)