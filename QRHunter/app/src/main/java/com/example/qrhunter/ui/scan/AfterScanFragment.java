package com.example.qrhunter.ui.scan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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
import com.example.qrhunter.data.model.Comment;
import com.example.qrhunter.data.model.Location;
import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.data.repository.QRCodeRepository;
import com.example.qrhunter.utils.QRCodeName;
import com.example.qrhunter.utils.QRCodeUtil;
import com.example.qrhunter.utils.QRCodeVisual;
import com.example.qrhunter.databinding.FragmentAfterScanBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * screen segment after scan
 */
public class AfterScanFragment extends Fragment {
    private FragmentAfterScanBinding binding;
    private String hashValue;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1001;
    ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    Bitmap savedPhoto = null;

    // location is 0,0 until user decides to record location
    private int latitude = 0;
    private int longitude = 0;
    boolean deletePhoto = false;
    boolean canSaveValue = false;
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
        hashValue = scanViewModel.getQRCodeHash().toString();

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
            //...

            // after location has been added
            Toast.makeText(view.getContext(), "added your location!", Toast.LENGTH_SHORT).show();
            binding.addGeoLocationButton.setImageResource(R.drawable.checkbox_location);
        });

        // set to get the image after taking the photo
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Bundle bundle = data.getExtras();
                            savedPhoto = (Bitmap) bundle.get("data");
                            savedPhoto = Bitmap.createScaledBitmap(savedPhoto, 640, 480, true);
                            Bitmap finalPhoto = (Bitmap) bundle.get("data");
                            finalPhoto = Bitmap.createScaledBitmap(savedPhoto, 640, 480, true);
                            binding.locationImage.setVisibility(View.VISIBLE);
                            binding.addPhotoLocationButton.setImageResource(R.drawable.remove_icon);
                            binding.locationImage.setImageBitmap(finalPhoto);

                            // the next time we click the icon is to delete the photo
                            deletePhoto = true;
                            checkRequirements();
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

            // we delete the picture
            if(deletePhoto == true) {
                savedPhoto = null;
                deletePhoto = false;
                binding.addPhotoLocationButton.setImageResource(R.drawable.add_icon);
                binding.locationImage.setVisibility(View.INVISIBLE);
                checkRequirements();
            }
            else {
                // we are not deleting the photo but taking a picture
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraActivityResultLauncher.launch(intent);
            }
        });

        binding.MoreOptions.setOnClickListener(view -> {
            if(canSaveValue == true){
                ArrayList<String> photos = new ArrayList<String>();
                photos.add(BitMapToString(savedPhoto));
                Location location = new Location(latitude, longitude, photos);
                QRCode newQRCode = new QRCode(hashValue, location, null);
                
                // add qrcode to database
                // QRCodeRepository.addQRCode(newQRCode);

                canSaveValue = false;
                
                // navigate to somewhere after this is done
                Toast.makeText(view.getContext(), "Successfully added your QRCode!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_after_scan_to_navigation_map);
            }
        });

        return binding.getRoot();
    }

    /**
     * to check the requirements are all met before storing the object to data base and finish
     * and go back to main
     */
    public void checkRequirements(){
        // no need to add geolocation due to privacy reasons
        if(savedPhoto != null){
            canSaveValue = true;
            binding.MoreOptions.setImageResource(R.drawable.check_mark);
        }
        else{
            canSaveValue = false;
            binding.MoreOptions.setImageResource(R.drawable.more_options);
        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos =new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

}