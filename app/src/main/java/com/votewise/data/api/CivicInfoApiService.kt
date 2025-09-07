package com.votewise.data.api

import com.votewise.app.BuildConfig
import com.votewise.data.model.CivicInfoResponse
import com.votewise.data.model.ElectionResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CivicInfoApiService {
    @GET("elections")
    suspend fun getElections(@Query("key") apiKey: String): ElectionResponse

    @GET("voterinfo")
    suspend fun getVoterInfo(
        @Query("address") address: String,
        @Query("electionId") electionId: String? = null,
        @Query("key") key: String
    ): CivicInfoResponse
}