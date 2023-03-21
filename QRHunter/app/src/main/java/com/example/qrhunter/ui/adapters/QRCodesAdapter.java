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
import com.example.qrhunter.ui.other_profile.OtherProfileFragmentDirections;
import com.example.qrhunter.ui.profile.ProfileFragmentDirections;

import java.util.ArrayList;

/**
 * Adapter for recycler view for QR Codes. Used in profile pages
 */
public class QRCodesAdapter extends RecyclerView.Adapter<QRCodesAdapter.ViewHolder> {
    private ArrayList<QRCode> qrCodes;
    private boolean isOtherPlayer;
    private onClickListeners listeners;

    /**
     * Constructor for QRCodesAdapter
     *
     * @param qrCodes       The data for this should be retrieved from a ViewModel class
     * @param isOtherPlayer Whether the qr codes displayed would be qr codes scanned by other player
     */
    public QRCodesAdapter(ArrayList<QRCode> qrCodes, boolean isOtherPlayer) {
        this.qrCodes = qrCodes;
        this.isOtherPlayer = isOtherPlayer;
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

        // If it's other player's qr code, don't let show the delete button
        holder.deleteQRCodeButton.setVisibility(View.INVISIBLE);

        holder.expandQRCodeButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);

            if (isOtherPlayer) {
                OtherProfileFragmentDirections.ActionOtherProfileFragmentToQrCodeFragment action =
                        OtherProfileFragmentDirections.actionOtherProfileFragmentToQrCodeFragment(qrCode.getId());
                navController.navigate(action);
            } else {
                ProfileFragmentDirections.ActionNavigationProfileToQrCodeFragment action =
                        ProfileFragmentDirections.actionNavigationProfileToQrCodeFragment(qrCode.getId());
                navController.navigate(action);
            }


        });

    }

    @Override
    public int getItemCount() {
        return qrCodes.size();
    }

    public void setOnClickListeners(onClickListeners listeners) {
        this.listeners = listeners;
    }

    /**
     * Listener interface for parent fragment / activity to implement
     */
    public interface onClickListeners {
        void onDeleteButtonClick(int position);
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

            deleteQRCodeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listeners != null) {
                        listeners.onDeleteButtonClick(getAdapterPosition());
                    }
                }
            });
        }


    }

}
