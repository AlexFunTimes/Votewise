package com.votewise.data.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface PlacesApiService {
    @POST("v1/places:autocomplete")
    suspend fun autocomplete(
        @Body request: AutocompleteRequest,
        @Header("X-Goog-Api-Key") apiKey: String,
        @Header("Content-Type") contentType: String = "application/json"
    ): AutocompleteResponse

    @GET("v1/places/{placeId}")
    suspend fun placeDetails(
        @Path("placeId") placeId: String,
        @Query("fields") fields: String,
        @Header("X-Goog-Api-Key") apiKey: String,
        @Header("Content-Type") contentType: String = "application/json"
    ): PlaceDetailsResponse
}

data class AutocompleteRequest(
    val input: String,
    val languageCode: String = "en-US"
)

data class AutocompleteResponse(
    val suggestions: List<AutocompleteSuggestion>
)

data class AutocompleteSuggestion(
    val placePrediction: PlacePrediction
)

data class PlacePrediction(
    val place: String,
    val placeId: String,
    val text: PlaceText
)

data class PlaceText(
    @SerializedName("text")
    val fullText: String
)

data class PlaceDetailsResponse(
    val formattedAddress: String
)
