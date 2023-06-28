package com.example.appfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.appfood.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    SupportMapFragment supportMapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Places.initialize(getApplicationContext(), "AIzaSyAVFdwAOWDulEolfduVUKOYybP9rW6g-gU");
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        setSearch();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng initialPosition = new LatLng(21.028502980142104, 105.83404398833528);
        float zoomLevel = 20;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, zoomLevel));
        mMap.addMarker(new MarkerOptions().position(initialPosition).title("Hà Nội"));
    }

    private void setSearch(){
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.searchMap);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
//        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteFragment.setCountry("84");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Log.e("AutocompleteError", status.getStatusMessage());
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng location = place.getLatLng();
                String placeName = place.getName();
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(location).title(placeName));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
            }
        });
    }
}