package com.votewise.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val PLACES_API_URL = "https://places.googleapis.com/"
    private const val CIVIC_INFO_API_URL = "https://www.googleapis.com/civicinfo/v2/"

    val placesApi: PlacesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(PLACES_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlacesApiService::class.java)
    }
    
    val civicInfoApi: CivicInfoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(CIVIC_INFO_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CivicInfoApiService::class.java)
    }
}