package com.example.qrhunter.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrhunter.R;
import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.ui.profile.ProfileFragmentDirections;

import java.util.ArrayList;

/**
 * Adapter for recycler view for QR Codes. Used in profile pages
 */
public class QRCodesAdapter extends RecyclerView.Adapter<QRCodesAdapter.ViewHolder> {
    private ArrayList<QRCode> qrCodes;

    /**
     * Constructor for QRCodesAdapter
     *
     * @param qrCodes The data for this should be retrieved from a ViewModel class
     */
    public QRCodesAdapter(ArrayList<QRCode> qrCodes) {
        this.qrCodes = qrCodes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qr_code, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QRCode qrCode = qrCodes.get(position);

        holder.qrCodeNameTextView.setText(qrCode.getName());
        holder.qrCodeScoreTextView.setText(Double.toString(qrCode.getScore()));

        // TODO: Set the listeners for the buttons
        holder.expandQRCodeButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);

            ProfileFragmentDirections.ActionNavigationProfileToQrCodeFragment action = ProfileFragmentDirections.actionNavigationProfileToQrCodeFragment(qrCode.getId());
            navController.navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        return qrCodes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView qrCodeNameTextView;
        public TextView qrCodeScoreTextView;
        public ImageButton deleteQRCodeButton;
        public ImageButton expandQRCodeButton;

        public ViewHolder(View itemView) {
            super(itemView);

            qrCodeNameTextView = itemView.findViewById(R.id.qr_code_name);
            qrCodeScoreTextView = itemView.findViewById(R.id.qr_code_score);
            deleteQRCodeButton = itemView.findViewById(R.id.qr_code_delete_button);
            expandQRCodeButton = itemView.findViewById(R.id.qr_code_expand_button);
        }
    }

}
