package com.example.qrhunter.ui.scan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.qrhunter.MainActivity;
import com.example.qrhunter.R;
import com.example.qrhunter.utils.QRCodeName;
import com.example.qrhunter.utils.QRCodeUtil;
import com.example.qrhunter.utils.QRCodeVisual;
import com.example.qrhunter.databinding.FragmentAfterScanBinding;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * screen segment after scan
 */
public class AfterScanFragment extends Fragment {
    private FragmentAfterScanBinding binding;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1001;

    ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get ViewModel
        ScanViewModel scanViewModel = new ViewModelProvider(this).get(ScanViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentAfterScanBinding.inflate(inflater, container, false);

        // get the hashValue and set the visual, name and score to this
        // change this part later after the hashValue is implemented
        String hashValue = scanViewModel.getQRCodeHash().toString();

        // display the views
        binding.QRVisual.setText(QRCodeUtil.generateVisualRepresentation(hashValue));
        binding.QRName.setText(QRCodeUtil.generateName(hashValue));
        // put the score here
        binding.QRCodeScoretext.setText(String.valueOf(QRCodeUtil.generateScore(hashValue))+ " Points");

        // put the return button
        binding.qrBackButton.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Returned to main page!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
        });

        // adding the geo location
        binding.addGeoLocationButton.setOnClickListener(view -> {
            // put add geo location on here
        });

        // set to get teh image after taking the photo
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Bundle bundle = data.getExtras();
                            Bitmap finalPhoto = (Bitmap) bundle.get("data");
                            binding.locationImage.setVisibility(View.VISIBLE);
                            binding.addPhotoLocationButton.setImageResource(R.drawable.checkbox_location);
                            binding.locationImage.setImageBitmap(finalPhoto);
                        }
                    }
                });

        // open the camera functionality and take a picture of the location
        binding.addPhotoLocationButton.setOnClickListener(view -> {
            //request permission to use the camera if permission is not granted
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraActivityResultLauncher.launch(intent);
        });


        return binding.getRoot();
    }


}