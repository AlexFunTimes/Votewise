package com.votewise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "candidate_voting_records")
data class VotingRecord(
    @PrimaryKey val id: String,
    val candidateId: String,
    val billNumber: String,
    val billTitle: String,
    val vote: VoteType,
    val date: String,
    val chamber: String,
    val session: String
)

enum class VoteType {
    YES,
    NO,
    ABSTAIN,
    NOT_VOTING,
    PRESENT
}
