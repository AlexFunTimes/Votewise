package com.votewise.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://api.example.com/"
    private const val CIVIC_INFO_BASE_URL = "https://www.googleapis.com/civic/v2/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val civicInfoRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(CIVIC_INFO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: VoteWiseApiService by lazy {
        retrofit.create(VoteWiseApiService::class.java)
    }

    val civicInfoApiService: CivicInfoApiService by lazy {
        civicInfoRetrofit.create(CivicInfoApiService::class.java)
    }
}
