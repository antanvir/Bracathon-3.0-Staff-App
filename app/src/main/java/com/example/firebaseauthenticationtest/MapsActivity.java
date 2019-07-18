package com.example.firebaseauthenticationtest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.firebaseauthenticationtest.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.firebaseauthenticationtest.R.id.editTextTitle;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Location currentLocation;
    LatLng latLng;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private Marker marker;
    private GoogleMap mMap;
    private String markerTitle = "My Current Location", passedUsername;
    EditText title;

    private static final int LOCATION_REQUEST_CODE = 101;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        passedUsername = getIntent().getExtras().getString("arg");
        title = findViewById(editTextTitle);
        findViewById(R.id.buttonLocShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationUpdate();
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        fetchLastLocation();
    }

    public void  locationUpdate(){
        final String identifier = title.getText().toString().trim();


        if(identifier.isEmpty()){
            title.setError("Marker identifier is required");
            title.requestFocus();
            return;
        }

        Village vil = new Village(passedUsername, currentLocation.getLatitude(), currentLocation.getLongitude());

        FirebaseDatabase.getInstance().getReference("Village Organizations").child(identifier)
                .setValue(vil )
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            markerTitle = identifier;
                            Toast.makeText(MapsActivity.this, "Location set & shared", Toast.LENGTH_LONG).show();

                            title.setText("");
                            Object googleMap;
                            onMapReady(mMap);


                        } else {
                            //display a failure message
                            Toast.makeText(getApplicationContext(), "Locaion sharing failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fetchLastLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(MapsActivity.this,currentLocation.getLatitude()+","+currentLocation.getLongitude(),Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }else{
                    Toast.makeText(MapsActivity.this,"No Location recorded",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        //MarkerOptions are used to create a new Marker.You can specify location, title etc with MarkerOptions
        //MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are Here");
        marker = googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(markerTitle));

        marker.showInfoWindow();

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F));

        //Adding the created the marker on the map
        //googleMap.addMarker(markerOptions);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                } else {
                    Toast.makeText(MapsActivity.this,"Location permission missing",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


}