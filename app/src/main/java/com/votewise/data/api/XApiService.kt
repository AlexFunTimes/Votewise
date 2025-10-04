package com.votewise.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface XApiService {
    @GET("2/tweets/search/recent")
    suspend fun searchTweets(
        @Header("Authorization") bearerToken: String,
        @Query("query") query: String,
        @Query("max_results") maxResults: Int = 10,
        @Query("tweet.fields") tweetFields: String = "created_at,author_id,public_metrics,context_annotations",
        @Query("user.fields") userFields: String = "username,name,verified",
        @Query("expansions") expansions: String = "author_id"
    ): Response<XSearchResponse>

    @GET("2/users/by/username/{username}")
    suspend fun getUserByUsername(
        @Header("Authorization") bearerToken: String,
        @Query("username") username: String,
        @Query("user.fields") userFields: String = "id,username,name,verified,public_metrics"
    ): Response<XUserResponse>

    @GET("2/users/{id}/tweets")
    suspend fun getUserTweets(
        @Header("Authorization") bearerToken: String,
        @Query("id") userId: String,
        @Query("max_results") maxResults: Int = 10,
        @Query("tweet.fields") tweetFields: String = "created_at,public_metrics,context_annotations"
    ): Response<XUserTweetsResponse>
}

data class XSearchResponse(
    val data: List<XTweet>?,
    val includes: XIncludes?,
    val meta: XMeta?
)

data class XUserResponse(
    val data: XUser?
)

data class XUserTweetsResponse(
    val data: List<XTweet>?,
    val meta: XMeta?
)

data class XTweet(
    val id: String,
    val text: String,
    val author_id: String?,
    val created_at: String,
    val public_metrics: XPublicMetrics?,
    val context_annotations: List<XContextAnnotation>?
)

data class XUser(
    val id: String,
    val username: String,
    val name: String,
    val verified: Boolean?,
    val public_metrics: XPublicMetrics?
)

data class XIncludes(
    val users: List<XUser>?
)

data class XMeta(
    val result_count: Int,
    val next_token: String?
)

data class XPublicMetrics(
    val retweet_count: Int?,
    val like_count: Int?,
    val reply_count: Int?,
    val quote_count: Int?,
    val followers_count: Int?,
    val following_count: Int?,
    val tweet_count: Int?
)

data class XContextAnnotation(
    val domain: XDomain?,
    val entity: XEntity?
)

data class XDomain(
    val id: String,
    val name: String,
    val description: String?
)

data class XEntity(
    val id: String,
    val name: String,
    val description: String?
)


