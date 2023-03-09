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
        binding.QRCodeScoretext.setText(String.valueOf(QRCodeUtil.generateScore(hashValue)));
        // put the return button
        binding.qrBackButton.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Returned to main page!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
        });
        return binding.getRoot();
    }


}