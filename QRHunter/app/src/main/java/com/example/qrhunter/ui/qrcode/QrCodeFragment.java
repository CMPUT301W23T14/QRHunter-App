package com.example.qrhunter.ui.qrcode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrhunter.data.model.Comment;
import com.example.qrhunter.databinding.FragmentQrCodeBinding;
import com.example.qrhunter.ui.adapters.CommentAdapter;
import com.example.qrhunter.ui.profile.ProfileViewModel;

import java.util.ArrayList;

public class QrCodeFragment extends Fragment {

    FragmentQrCodeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Get qr code ViewModel
        QrCodeViewModel qrCodeViewModel = new ViewModelProvider(this).get(QrCodeViewModel.class);

        // Get Profile ViewModel to access current player information
        ProfileViewModel profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentQrCodeBinding.inflate(inflater, container, false);

        // Get qr code id from destination argument
        String qrCodeId = QrCodeFragmentArgs.fromBundle(getArguments()).getQrCodeId();

        // Get recycler view
        RecyclerView rvComments = binding.commentRecyclerView;

        // List of qr codes for the recycler view adapter
        ArrayList<Comment> comments = new ArrayList<>();

        // Set up recycler view
        CommentAdapter commentAdapter = new CommentAdapter(comments);

        rvComments.setAdapter(commentAdapter);
        rvComments.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Bind data to ui
        qrCodeViewModel.getQRCode(qrCodeId)
                .observe(getViewLifecycleOwner(), qrCode -> {
                    if (qrCode != null) {
                        binding.QRName.setText(qrCode.getName());
                        binding.QRCodeScoretext.setText(Double.toString(qrCode.getScore()));
                        binding.QRVisual.setText(qrCode.getVisualRepresentation());
                        binding.QRCodeAddresstext.setText(qrCodeViewModel.getAddress(qrCode, getContext()));

                        // Get and bind the amount of players who scanned the code
                        qrCodeViewModel.getScannedBy(qrCode).observe(getViewLifecycleOwner(), amountScannedBy -> {
                            if (amountScannedBy > 0) {
                                binding.scannedByText.setText(amountScannedBy.toString() + " players");
                            }
                        });
                        // Get and bind the comments
                        qrCodeViewModel.getComments(qrCode).observe(getViewLifecycleOwner(), newComments -> {
                            comments.clear();
                            comments.addAll(newComments);
                            commentAdapter.notifyDataSetChanged();
                        });

                        binding.newCommentTextLayout.setEndIconOnClickListener(v -> {
                            String author = profileViewModel.getPlayer().getValue().getUsername();
                            Comment newComment = new Comment(author, binding.newCommentEditText.getText().toString());

                            qrCodeViewModel.addComment(qrCode, newComment);
                            binding.newCommentEditText.setText("");
                            binding.newCommentEditText.clearFocus();
                        });
                    }
                });

        binding.qrBackButton.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).popBackStack();
        });

        return binding.getRoot();
    }


}