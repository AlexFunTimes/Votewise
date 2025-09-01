package com.votewise.data

import android.util.Log
import com.votewise.app.BuildConfig
import com.votewise.data.model.CivicInfoResponse
import com.votewise.data.api.CivicInfoApiService
import com.votewise.data.model.Result

class CandidateRepository(
    private val civicInfoApiService: CivicInfoApiService
) {

    suspend fun getVoterInfo(address: String): Result<CivicInfoResponse> {
        if (address.isEmpty()) {
            return Result.Error(Exception("Address cannot be empty."))
        }
        Log.d("CandidateRepository", "Fetching voter info for address: $address")
        return try {
            val civicInfoResponse = civicInfoApiService.getVoterInfo(
                address = address,
                apiKey = BuildConfig.GOOGLE_CIVIC_API_KEY
            )
            Log.d("CandidateRepository", "Received voter info response: $civicInfoResponse")
            Result.Success(civicInfoResponse)
        } catch (e: Exception) {
            Log.e("CandidateRepository", "Error fetching voter info: ${e.message}", e)
            Result.Error(e)
        }
    }
}
