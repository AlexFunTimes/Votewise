package com.votewise.data.api

import com.votewise.data.model.Candidate
import com.votewise.data.model.Donor
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FecApiService {
    @GET("candidates/")
    suspend fun getCandidates(
        @Query("api_key") apiKey: String,
        @Query("cycle") cycle: Int,
        @Query("state") state: String? = null,
        @Query("district") district: String? = null,
        @Query("office") office: String? = null,
        @Query("party") party: String? = null,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): Response<FecCandidatesResponse>

    @GET("candidates/{candidate_id}/")
    suspend fun getCandidateDetails(
        @Query("api_key") apiKey: String,
        @Query("candidate_id") candidateId: String,
        @Query("cycle") cycle: Int
    ): Response<FecCandidateResponse>

    @GET("schedules/schedule_a/")
    suspend fun getDonations(
        @Query("api_key") apiKey: String,
        @Query("candidate_id") candidateId: String,
        @Query("cycle") cycle: Int,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 100
    ): Response<FecDonationsResponse>
}

data class FecCandidatesResponse(
    val results: List<FecCandidate>,
    val pagination: FecPagination
)

data class FecCandidate(
    val candidate_id: String,
    val name: String,
    val party: String,
    val office: String,
    val state: String?,
    val district: String?,
    val incumbent_challenge: String?,
    val candidate_status: String?
)

data class FecCandidateResponse(
    val results: List<FecCandidateDetail>
)

data class FecCandidateDetail(
    val candidate_id: String,
    val name: String,
    val party: String,
    val office: String,
    val state: String?,
    val district: String?,
    val incumbent_challenge: String?,
    val candidate_status: String?,
    val principal_committees: List<FecCommittee>?
)

data class FecCommittee(
    val committee_id: String,
    val name: String,
    val committee_type: String
)

data class FecDonationsResponse(
    val results: List<FecDonation>,
    val pagination: FecPagination
)

data class FecDonation(
    val contributor_name: String,
    val contributor_zip: String?,
    val contributor_employer: String?,
    val contributor_occupation: String?,
    val contributor_type: String?,
    val contribution_receipt_amount: Double,
    val contribution_receipt_date: String,
    val committee: FecCommittee?
)

data class FecPagination(
    val page: Int,
    val per_page: Int,
    val count: Int,
    val pages: Int
)


