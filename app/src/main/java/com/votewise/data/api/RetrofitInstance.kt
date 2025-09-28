package com.votewise.data.api

import com.votewise.app.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val PLACES_API_URL = "https://places.googleapis.com/"
    private const val CIVIC_INFO_API_URL = "https://www.googleapis.com/civicinfo/v2/"
    private const val FEC_BASE_URL = "https://api.open.fec.gov/v1/"
    private const val OPENSECRETS_BASE_URL = "https://www.opensecrets.org/api/"
    private const val VOTESMART_BASE_URL = "http://api.votesmart.org/"
    private const val NEWS_API_BASE_URL = "https://newsapi.org/v2/"
    private const val X_API_BASE_URL = "https://api.twitter.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val placesApi: PlacesApiService by lazy {
        createRetrofit(PLACES_API_URL).create(PlacesApiService::class.java)
    }
    
    val civicInfoApi: CivicInfoApiService by lazy {
        createRetrofit(CIVIC_INFO_API_URL).create(CivicInfoApiService::class.java)
    }

    val fecApi: FecApiService by lazy {
        createRetrofit(FEC_BASE_URL).create(FecApiService::class.java)
    }

    val openSecretsApi: OpenSecretsApiService by lazy {
        createRetrofit(OPENSECRETS_BASE_URL).create(OpenSecretsApiService::class.java)
    }

    val voteSmartApi: VoteSmartApiService by lazy {
        createRetrofit(VOTESMART_BASE_URL).create(VoteSmartApiService::class.java)
    }

    val newsApi: NewsApiService by lazy {
        createRetrofit(NEWS_API_BASE_URL).create(NewsApiService::class.java)
    }

    val xApi: XApiService by lazy {
        createRetrofit(X_API_BASE_URL).create(XApiService::class.java)
    }

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}