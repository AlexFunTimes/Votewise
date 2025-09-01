package com.votewise.data.model

data class ElectionResponse(
    val elections: List<Election>
)

data class Election(
    val id: String,
    val name: String,
    val electionDay: String,
    val ocdDivisionId: String
)
