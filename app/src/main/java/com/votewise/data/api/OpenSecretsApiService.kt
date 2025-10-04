package com.votewise.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenSecretsApiService {
    @GET("getLegislators")
    suspend fun getLegislators(
        @Query("apikey") apiKey: String,
        @Query("id") state: String? = null
    ): Response<OpenSecretsResponse<OpenSecretsLegislator>>

    @GET("candContrib")
    suspend fun getCandidateContributions(
        @Query("apikey") apiKey: String,
        @Query("cid") candidateId: String,
        @Query("cycle") cycle: Int
    ): Response<OpenSecretsResponse<OpenSecretsContribution>>

    @GET("candIndustry")
    suspend fun getCandidateIndustries(
        @Query("apikey") apiKey: String,
        @Query("cid") candidateId: String,
        @Query("cycle") cycle: Int
    ): Response<OpenSecretsResponse<OpenSecretsIndustry>>

    @GET("candSummary")
    suspend fun getCandidateSummary(
        @Query("apikey") apiKey: String,
        @Query("cid") candidateId: String,
        @Query("cycle") cycle: Int
    ): Response<OpenSecretsResponse<OpenSecretsSummary>>
}

data class OpenSecretsResponse<T>(
    val response: OpenSecretsData<T>
)

data class OpenSecretsData<T>(
    val legislators: List<T>? = null,
    val contributors: List<T>? = null,
    val industries: List<T>? = null,
    val summary: List<T>? = null
)

data class OpenSecretsLegislator(
    val cid: String,
    val firstlast: String,
    val party: String,
    val office: String,
    val state: String,
    val district: String?,
    val photo: String?
)

data class OpenSecretsContribution(
    val contributor: String,
    val amount: Double,
    val pacs: String?,
    val indivs: String?
)

data class OpenSecretsIndustry(
    val industry: String,
    val indivs: Double,
    val pacs: Double,
    val total: Double
)

data class OpenSecretsSummary(
    val cand_name: String,
    val cid: String,
    val cycle: Int,
    val state: String,
    val party: String,
    val chamber: String,
    val first_elected: String?,
    val next_election: String?,
    val total: Double,
    val spent: Double,
    val cash_on_hand: Double,
    val debt: Double
)


