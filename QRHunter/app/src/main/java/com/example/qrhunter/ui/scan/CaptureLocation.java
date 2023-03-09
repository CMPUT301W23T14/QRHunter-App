package com.example.qrhunter.ui.scan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.qrhunter.R;

import javax.annotation.Nullable;

public class CaptureLocation extends FragmentActivity {
    ImageView imageView;
    ImageButton button;

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 100;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_after_scan);

        imageView = findViewById(R.id.locationImage);
        button = findViewById(R.id.addPhotoLocationButton);

        //request permission to use the camera if permission is not granted
        if (ContextCompat.checkSelfPermission(CaptureLocation.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(CaptureLocation.this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraActivityResultLauncher.launch(intent);
            }
        });

        // we can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Bundle bundle = data.getExtras();
                            Bitmap finalPhoto = (Bitmap) bundle.get("data");
                            imageView.setImageBitmap(finalPhoto);
                        }
                    }
                });
    }
}
