package com.votewise.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.votewise.app.BuildConfig
import com.votewise.data.CandidateRepository
import com.votewise.data.model.CivicInfoResponse
import com.votewise.data.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val candidateRepository: CandidateRepository
) : ViewModel() {

    private val _voterInfo = MutableStateFlow<Result<CivicInfoResponse>>(Result.Loading)
    val voterInfo: StateFlow<Result<CivicInfoResponse>> = _voterInfo

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun findCandidatesByFullAddress(address: String, onFinished: () -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = candidateRepository.getVoterInfo(address, BuildConfig.GOOGLE_CIVIC_API_KEY)
            _voterInfo.value = result
            if (result is Result.Success || result is Result.Error) {
                onFinished()
            }
            _isLoading.value = false
        }
    }
}
