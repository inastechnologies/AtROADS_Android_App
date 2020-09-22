package com.inas.atroads.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.inas.atroads.R;
import com.inas.atroads.views.Activities.HomeMapsActivity;

import java.util.Arrays;
import java.util.List;

public class PlacesActivity extends AppCompatActivity {

    private static final String TAG = "PlacesActivity";
    private EditText PinEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_maps);

// Initialize Places.
        Places.initialize(getApplicationContext(), getString(R.string.google_api_key));

// Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        placesClient.fetchPlace(new FetchPlaceRequest() {
            @NonNull
            @Override
            public String getPlaceId() {

                return null;
            }

            @NonNull
            @Override
            public List<Place.Field> getPlaceFields() {
                return null;
            }

            @Nullable
            @Override
            public AutocompleteSessionToken getSessionToken() {
                return null;
            }

            @Nullable
            @Override
            public CancellationToken getCancellationToken() {
                return null;
            }
        });

        PlacesSelection();




        // Define a Place ID.
        String placeId = "ChIJgzG0W8WQyzsRAyfuI1sTvBo";

// Specify the fields to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

// Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields)
                .build();

        // Add a listener to handle the response.
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            Log.i(TAG, "Place found: " + place.getName());
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                // Handle error with given status code.
                Log.e(TAG, "Place not found: " + exception.getMessage());
            }
        });

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();

            // Get the photo metadata.
            PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);

            // Get the attribution text.
            String attributions = photoMetadata.getAttributions();

            // Create a FetchPhotoRequest.
            FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(500) // Optional.
                    .setMaxHeight(300) // Optional.
                    .build();
            placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                Bitmap bitmap = fetchPhotoResponse.getBitmap();
//                imageView.setImageBitmap(bitmap);
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    int statusCode = apiException.getStatusCode();
                    // Handle error with given status code.
                    Log.e(TAG, "Place not found: " + exception.getMessage());
                }
            });
        });
        PinEditText = findViewById(R.id.PinEditText);
        PinEditText.setInputType(InputType.TYPE_NULL);
        PinEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
                AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        // Create a RectangularBounds object.
                RectangularBounds bounds = RectangularBounds.newInstance(
                        new LatLng(-33.880490, 151.184363),
                        new LatLng(-33.858754, 151.229596));
        // Use the builder to create a FindAutocompletePredictionsRequest.
                FindAutocompletePredictionsRequest findAutocompletePredictionsRequest = FindAutocompletePredictionsRequest.builder()
        // Call either setLocationBias() OR setLocationRestriction().
                        .setLocationBias(bounds)
                        //.setLocationRestriction(bounds)
                        .setCountry("au")
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setSessionToken(token)
                        .setQuery("query")
                        .build();

                placesClient.findAutocompletePredictions(findAutocompletePredictionsRequest).addOnSuccessListener((response) -> {
                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                        Log.i(TAG, prediction.getPlaceId());
                        Log.i(TAG, prediction.getPrimaryText(null).toString());
                    }
                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                });
            }
        });


    }


    /*
     PlacesSelection
    */
    private void PlacesSelection()
    {
        AutocompleteSupportFragment autocomplete_fragmentpin = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.Pin_autocomplete_fragment);
        autocomplete_fragmentpin.getView().findViewById(R.id.Pin_autocomplete_fragment).setVisibility(View.GONE);
// Specify the types of place data to return.
        autocomplete_fragmentpin.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

// Set up a PlaceSelectionListener to handle the response.
        autocomplete_fragmentpin.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("GMapsScreen", "Place: " + place.getName() + ", " + place.getId());
                Log.i("GMapsScreen", "Place: "+ place.getLatLng()+" ,"+ place.getAddress()+","+place.getViewport()+"\n"+ place.getAddressComponents());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("GMapsScreen", "An error occurred: " + status);
            }
        });

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.Drop_autocomplete_fragment);
        autocompleteFragment.getView().findViewById(R.id.Drop_autocomplete_fragment).setVisibility(View.GONE);
// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

// Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("GMapsScreen", "Place: " + place.getName() + ", " + place.getId());
                Log.i("GMapsScreen", "Place: "+ place.getLatLng()+" ,"+ place.getAddress()+","+place.getViewport()+"\n"+ place.getAddressComponents());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("GMapsScreen", "An error occurred: " + status);
            }
        });
    }


}