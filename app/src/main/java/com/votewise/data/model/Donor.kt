package com.votewise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "donors")
data class Donor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val candidateId: String,
    val donorName: String,
    val amount: Double,
    val date: String
)
