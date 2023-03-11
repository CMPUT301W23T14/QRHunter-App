package com.example.qrhunter.ui.map;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrhunter.R;
import com.example.qrhunter.databinding.FragmentMapBinding;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.Documentation;

import java.net.URL;

public class MapFragment extends Fragment {
    private FragmentMapBinding binding;
    private GoogleMap map;
    // Default location (Sydney, Australia) and default zoom to use when location permission is not granted
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;
    /*
    Asks for location permissions and updates map view to current location if permission is granted
    Website: Stackoverflow
    URL: https://stackoverflow.com/questions/66551781/android-onrequestpermissionsresult-is-deprecated-are-there-any-alternatives
    Author: https://stackoverflow.com/users/10247147/daniel-wang
    */
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    locationPermissionGranted = true;
                    SupportMapFragment supportMapFragment=(SupportMapFragment)
                            getChildFragmentManager().findFragmentById(R.id.google_map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            map = googleMap;
                            updateLocationUI();
                            getDeviceLocation();
                        }
                    });
                }
                updateLocationUI();
            }
    );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Get ViewModel
        MapViewModel mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater, container, false);
        if (!locationPermissionGranted) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            updateLocationUI();
            getDeviceLocation();
        }
        return binding.getRoot();
    }
    /**
     * Enables or disables location button based on permissions granted
     */
    /*
    Website: Google Developer Documentation
    URL: https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
    */
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    /*
    Website: Google Developer Documentation
    URL: https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
    Website: Stackoverflow
    URL: https://stackoverflow.com/questions/67441949/location-on-google-map-in-android-studio-not-updating"
    Author: "https://stackoverflow.com/users/12660050/vatsal-dholakiya"
    */
    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                LocationManager lm = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
                boolean gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if(gps_enabled) {
                    lastKnownLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastKnownLocation != null) {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(lastKnownLocation.getLatitude(),
                                        lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    } else {
                        map.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                    }
                }
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
}