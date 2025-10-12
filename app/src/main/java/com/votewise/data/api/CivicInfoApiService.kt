package com.votewise.data.api

import com.votewise.app.BuildConfig
import com.votewise.data.model.CivicInfoResponse
import com.votewise.data.model.ElectionResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CivicInfoApiService {
    @GET("elections")
    suspend fun getElections(@Query("key") apiKey: String = BuildConfig.GOOGLE_CIVIC_API_KEY): ElectionResponse

    @GET("voterinfo")
    suspend fun getVoterInfoByAddress(
        @Query("address") address: String,
        @Query("electionId") electionId: String?,
        @Query("key") key: String = BuildConfig.GOOGLE_CIVIC_API_KEY
    ): CivicInfoResponse
}
