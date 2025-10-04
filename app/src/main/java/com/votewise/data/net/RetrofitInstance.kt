package com.votewise.data.net

import com.votewise.data.api.CivicInfoApiService
import com.votewise.data.api.PlacesApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    val civicInfoApi: CivicInfoApiService by lazy {
        retrofit.create(CivicInfoApiService::class.java)
    }

    val placesApi: PlacesApiService by lazy {
        retrofit.create(PlacesApiService::class.java)
    }
}