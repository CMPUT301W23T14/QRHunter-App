package com.example.qrhunter.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrhunter.R;

import java.util.ArrayList;

/**
 * An adapter for displaying address in the QR Code page
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private ArrayList<String> addresses;

    public AddressAdapter(ArrayList<String> address) {
        this.addresses = address;
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        String text = addresses.get(position);
        holder.addressItem.setText(text);
    }
    /**
     * Gets the number of addresses
     * @return The number of addresses
     */
    @Override
    public int getItemCount() {
        return this.addresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView addressItem;

        public ViewHolder(View itemView) {
            super(itemView);
            addressItem = (TextView) itemView;
        }
    }
}
