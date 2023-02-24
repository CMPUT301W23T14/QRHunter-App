package com.example.qrhunter.ui.scan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.qrhunter.R;
import com.example.qrhunter.databinding.FragmentAfterScanBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AfterScanFragment extends Fragment {
    private FragmentAfterScanBinding binding;
    //
    private TextView QRContents;
    private FirebaseFirestore db;

    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get ViewModel
        ScanViewModel scanViewModel = new ViewModelProvider(this).get(ScanViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentAfterScanBinding.inflate(inflater, container, false);

        // I have no idea why this dosen't work
//        scanViewModel.getScannedText().observe(getViewLifecycleOwner(), newString -> {
//            binding.button2.setText(newString);
//        });


        /**
         * Feel free to remove these, I just wanted to see if the data was being passed
         */
        db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("scanned_items_test"); // this is just a temporary collection
        // get scanned text from firebase
        usersRef.document("test").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String scannedText = task.getResult().getString("Scanned item(test)");
                binding.QRCONTENTS.setText(scannedText);
            }
        });
        binding.button2.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Returned", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
        });


        return binding.getRoot();
    }


}