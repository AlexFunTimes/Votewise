package com.votewise.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.votewise.ui.viewmodel.PlacesViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddressAutocompleteTextField(
    onQueryChanged: (String) -> Unit,
    onPlaceSelected: (Place) -> Unit,
    onSearchClick: () -> Unit
) {
    val context = LocalContext.current
    val placesClient = remember { Places.createClient(context) }
    val placesViewModel: PlacesViewModel = viewModel()
    val predictions by placesViewModel.locationPredictions.collectAsState()
    var query by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

    Column {
        TextField(
            value = query,
            onValueChange = {
                query = it
                onQueryChanged(it)
                placesViewModel.searchPlaces(it, placesClient)
            },
            label = { Text("Enter your address") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearchClick()
                keyboardController?.hide()
            })
        )

        predictions.forEach { prediction ->
            Text(
                text = prediction.getFullText(null).toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val request = FetchPlaceRequest.newInstance(prediction.placeId, placeFields)
                        placesClient.fetchPlace(request)
                            .addOnSuccessListener { response ->
                                val place = response.place
                                onPlaceSelected(place)
                                query = place.address.orEmpty()
                                onQueryChanged(query)
                                placesViewModel.clearPredictions()
                            }
                            .addOnFailureListener { exception ->
                                // Handle the error
                            }
                    }
                    .padding(8.dp)
            )
        }
    }
}