package com.example.appfood.activity;

import static android.content.ContentValues.TAG;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.appfood.R;
import com.example.appfood.adapter.PredictionAdapter;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.PlaceTypes;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    SupportMapFragment supportMapFragment;
    PlacesClient placesClient;
    ImageButton btnBack;
    FloatingActionButton btnPosition;
    AutoCompleteTextView autoCompleteTextView;
    RecyclerView listHint;
    PredictionAdapter predictionAdapter;
    List<String> predictions;
    private static final LatLng BOUNDS_NE = new LatLng(21.252440, 106.045688);
    private static final LatLng BOUNDS_SW = new LatLng(20.924507, 105.683119);
    private static final RectangularBounds BOUNDS = RectangularBounds.newInstance(BOUNDS_SW, BOUNDS_NE);
    private static final int FINE_PERMISSION_CODE = 1;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST,null);
        setVariable();
        setListHint();
        setEvent();
    }

    private void setVariable() {
        Places.initialize(getApplicationContext(), "AIzaSyAVFdwAOWDulEolfduVUKOYybP9rW6g-gU");
        placesClient = Places.createClient(this);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(MapActivity.this);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        listHint = (RecyclerView) findViewById(R.id.listHint);
        predictions = new ArrayList<>();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        btnPosition = (FloatingActionButton) findViewById(R.id.btnPosition);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng initialPosition = new LatLng(21.028502980142104, 105.83404398833528);
        float zoomLevel = 20;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, zoomLevel));
        mMap.addMarker(new MarkerOptions().position(initialPosition).title("Hà Nội"));
    }

    private void setListHint() {
        listHint.setLayoutManager(new LinearLayoutManager(this));
        predictionAdapter = new PredictionAdapter(this, predictions);
        listHint.setAdapter(predictionAdapter);
    }

    private void setEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.super.onBackPressed();
            }
        });

        btnPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getLocation();
//                if (currentLocation != null) {
//                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//                    float zoomLevel = 20;
//                    mMap.clear();
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
//                    mMap.addMarker(new MarkerOptions().position(latLng).title("Vị trí của bạn"));
//                }
                if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLastKnownLocation();
                } else {
                    ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
                }
            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = autoCompleteTextView.getText().toString().trim();
                if (!TextUtils.isEmpty(query)) {
                    performSearch(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                double latitude = lastKnownLocation.getLatitude();
                double longitude = lastKnownLocation.getLongitude();
                // Use the latitude and longitude values as needed
                Toast.makeText(MapActivity.this, "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MapActivity.this, "Last known location not available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }else{
                Toast.makeText(this, "Cần cấp quyền vị trí!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void performSearch(String query) {
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setCountries("VN")
                .setTypesFilter(listOf(PlaceTypes.ADDRESS))
                .setSessionToken(AutocompleteSessionToken.newInstance())
                .setQuery(query)
                .setLocationBias(BOUNDS)
                .build();

        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener((response) -> {
                    List<String> predictions = new ArrayList<>();
                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                        predictions.add(prediction.getFullText(null).toString());
                    }
                    predictionAdapter.setPredictions();
                    listHint.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener((exception) -> {
                    Log.e(TAG, exception.toString());
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                });
    }


}