package com.votewise.data.model

import androidx.room.Ignore

data class CivicInfoResponse(
    val kind: String? = null,
    val normalizedInput: Address? = null,
    val pollingLocations: List<PollingLocation>? = null,
    val contests: List<Contest>? = null,
    val state: List<State>? = null
)

data class Address(
    val line1: String? = null,
    val city: String? = null,
    val state: String? = null,
    val zip: String? = null
)

data class PollingLocation(
    val id: String? = null,
    val address: Address? = null,
    val pollingHours: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val name: String? = null
)

data class Contest(
    val id: String? = null,
    val type: String? = null,
    val office: String? = null,
    val district: District? = null,
    val level: List<String>? = null,
    val roles: List<String>? = null,
    val sources: List<Source>? = null,
    val candidates: List<CivicInfoCandidate>? = null
)

data class District(
    val name: String? = null,
    val scope: String? = null,
    val id: String? = null
)

data class Source(
    val name: String? = null,
    val official: Boolean? = null
)

data class CivicInfoCandidate(
    val name: String? = null,
    val party: String? = null,
    val candidateUrl: String? = null,
    val phone: String? = null,
    val photoUrl: String? = null,
    val email: String? = null,
    val channels: List<Channel>? = null
)

data class Channel(
    val type: String? = null,
    val id: String? = null
)

data class State(
    val name: String? = null,
    val electionAdministrationBody: ElectionAdministrationBody? = null,
    val sources: List<Source>? = null
)

data class ElectionAdministrationBody(
    val name: String? = null,
    val electionInfoUrl: String? = null,
    val voterRegistrationUrl: String? = null,
    val absenteeVotingInfoUrl: String? = null,
    val electionResultsUrl: String? = null,
    val primaryDataVoterRegistrationUrl: String? = null,
    val physicalAddress: Address? = null
)
