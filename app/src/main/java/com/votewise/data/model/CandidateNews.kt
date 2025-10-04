package com.votewise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "candidate_news")
data class CandidateNews(
    @PrimaryKey val id: String,
    val candidateId: String,
    val articleUrl: String,
    val imageUrl: String?,
    val title: String,
    val source: String,
    val publishedAt: Date
)
