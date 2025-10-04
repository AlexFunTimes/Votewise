package com.votewise.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VoteSmartApiService {
    @GET("Officials.getByZip")
    suspend fun getOfficialsByZip(
        @Query("key") apiKey: String,
        @Query("zip5") zipCode: String
    ): Response<VoteSmartResponse<VoteSmartOfficial>>

    @GET("Candidates.getByZip")
    suspend fun getCandidatesByZip(
        @Query("key") apiKey: String,
        @Query("zip5") zipCode: String,
        @Query("year") year: Int
    ): Response<VoteSmartResponse<VoteSmartCandidate>>

    @GET("Votes.getByOfficial")
    suspend fun getVotingRecord(
        @Query("key") apiKey: String,
        @Query("candidateId") candidateId: String
    ): Response<VoteSmartResponse<VoteSmartVote>>

    @GET("Rating.getCandidateRating")
    suspend fun getCandidateRating(
        @Query("key") apiKey: String,
        @Query("candidateId") candidateId: String
    ): Response<VoteSmartResponse<VoteSmartRating>>

    @GET("Bio.getBio")
    suspend fun getCandidateBio(
        @Query("key") apiKey: String,
        @Query("candidateId") candidateId: String
    ): Response<VoteSmartResponse<VoteSmartBio>>
}

data class VoteSmartResponse<T>(
    val officials: List<T>? = null,
    val candidates: List<T>? = null,
    val votes: List<T>? = null,
    val rating: List<T>? = null,
    val bio: List<T>? = null
)

data class VoteSmartOfficial(
    val candidateId: String,
    val title: String,
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val suffix: String?,
    val nickname: String?,
    val party: String,
    val office: String,
    val officeDistrict: String?,
    val state: String,
    val photo: String?
)

data class VoteSmartCandidate(
    val candidateId: String,
    val title: String,
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val suffix: String?,
    val nickname: String?,
    val party: String,
    val office: String,
    val officeDistrict: String?,
    val state: String,
    val photo: String?,
    val electionYear: Int
)

data class VoteSmartVote(
    val billId: String,
    val billNumber: String,
    val title: String,
    val vote: String,
    val date: String,
    val category: String?,
    val description: String?
)

data class VoteSmartRating(
    val candidateId: String,
    val ratingId: String,
    val timespan: String,
    val rating: String,
    val category: String,
    val sig: String
)

data class VoteSmartBio(
    val candidateId: String,
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val suffix: String?,
    val nickname: String?,
    val party: String,
    val office: String,
    val officeDistrict: String?,
    val state: String,
    val photo: String?,
    val education: String?,
    val profession: String?,
    val political: String?,
    val religion: String?,
    val congressionalDistrict: String?,
    val family: String?
)


