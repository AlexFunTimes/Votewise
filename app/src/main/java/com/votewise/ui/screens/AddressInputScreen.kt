package com.votewise.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.libraries.places.api.model.Place
import com.votewise.app.R
import com.votewise.ui.components.AddressAutocompleteTextField
import com.votewise.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun AddressInputScreen(
    onNavigateToHome: () -> Unit,
    homeViewModel: HomeViewModel
) {
    var addressText by remember { mutableStateOf("") }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    val isLoading by homeViewModel.isLoading.collectAsState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.votewise_pioneers_words),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                AddressAutocompleteTextField(
                    onQueryChanged = { query ->
                        addressText = query
                        selectedPlace = null
                    },
                    onPlaceSelected = { place ->
                        selectedPlace = place
                        addressText = place.address.orEmpty()
                    },
                    onSearchClick = {
                        selectedPlace?.let {
                            scope.launch {
                                homeViewModel.findCandidatesByFullAddress(addressText).join()
                                onNavigateToHome()
                            }
                        }
                    }
                )
            }
        }
    }
}
