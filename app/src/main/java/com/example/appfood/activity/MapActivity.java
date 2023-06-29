package com.example.appfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.appfood.R;
import com.example.appfood.adapter.PredictionAdapter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    SupportMapFragment supportMapFragment;
    PlacesClient placesClient;
    ImageButton btnBack;
    EditText txtSearch;
    ListView listHint;
    PredictionAdapter predictionAdapter;
    ArrayList<String> arrayHint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setVariable();
        setListHint();
        setEvent();
    }

    private void setVariable(){
        Places.initialize(getApplicationContext(), "AIzaSyAVFdwAOWDulEolfduVUKOYybP9rW6g-gU");
        placesClient = Places.createClient(this);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        listHint = (ListView) findViewById(R.id.listHint);
        arrayHint = new ArrayList<>();
        predictionAdapter = new PredictionAdapter(this,R.layout.activity_prediction_item,arrayHint);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng initialPosition = new LatLng(21.028502980142104, 105.83404398833528);
        float zoomLevel = 20;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, zoomLevel));
        mMap.addMarker(new MarkerOptions().position(initialPosition).title("Hà Nội"));
    }

    private void setListHint(){
        listHint.setAdapter(predictionAdapter);
        listHint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedPrediction = arrayHint.get(position);
                txtSearch.setText(selectedPrediction);
                arrayHint.clear();
                predictionAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.super.onBackPressed();
            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();
                performAutocompletePrediction(query);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void performAutocompletePrediction(String query) {
        // Create a RectangularBounds instance to restrict the autocomplete results to a specific area (optional)
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(20.9517, 105.7547), // Southwest corner
                new LatLng(21.0774, 105.9025)   // Northeast corner
        );

        // Create a FindAutocompletePredictionsRequest instance
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setLocationBias(bounds)
                .setTypeFilter(TypeFilter.ADDRESS)
                .setQuery(query)
                .build();

        // Perform autocomplete predictions
        Task<FindAutocompletePredictionsResponse> task = placesClient.findAutocompletePredictions(request);
        task.addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
            @Override
            public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                if (task.isSuccessful()) {
                    FindAutocompletePredictionsResponse response = task.getResult();
                    List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
                    arrayHint.clear();
                    for (AutocompletePrediction prediction : predictions) {
                        arrayHint.add(prediction.getFullText(null).toString());
                    }
                    predictionAdapter.notifyDataSetChanged();
                } else {
                    // Handle the error
                    Exception exception = task.getException();
                    if (exception != null) {
                        Log.e("AutocompleteError", exception.getMessage());
                    }
                }
            }
        });
    }
}