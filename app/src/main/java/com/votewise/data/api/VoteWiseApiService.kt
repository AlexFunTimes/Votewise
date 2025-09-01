package com.votewise.data.api

import com.votewise.data.model.Candidate
import retrofit2.http.GET
import retrofit2.http.Query

interface VoteWiseApiService {
    @GET("candidates")
    suspend fun getCandidates(
        @Query("zipCode") zipCode: String,
        @Query("api_key") apiKey: String
    ): List<Candidate>
}
