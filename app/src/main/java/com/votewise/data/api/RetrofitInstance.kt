package com.votewise.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val VOTEWISE_BASE_URL = "https://api.votewise.com/" // Placeholder
    private const val CIVIC_INFO_BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

    val votewiseApi: VoteWiseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(VOTEWISE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VoteWiseApiService::class.java)
    }

    val civicInfoApi: CivicInfoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(CIVIC_INFO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CivicInfoApiService::class.java)
    }
}
