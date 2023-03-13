package com.example.qrhunter.ui.scan;

import android.graphics.Bitmap;
import android.util.Base64;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrhunter.data.model.Location;
import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.data.repository.QRCodeRepository;
import com.example.qrhunter.utils.QRCodeUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ScanViewModel extends ViewModel {
    private final MutableLiveData<String> qrCodeContent = new MutableLiveData<>();
    private final MutableLiveData<String> qrCodeHash = new MutableLiveData<>();
    private final MutableLiveData<Location> location = new MutableLiveData<>(new Location(0, 0, new ArrayList<>()));
    private QRCodeRepository qrCodeRepository = new QRCodeRepository();

    public LiveData<String> getQRCodeContent() {
        return qrCodeContent;
    }

    /**
     * Called when user scans a qr code
     *
     * @param qrCodeContent The content of the QR Code
     */
    public void scanQRCode(String qrCodeContent) {
        this.qrCodeContent.setValue(qrCodeContent);
        // turn it into hashValue
        this.qrCodeHash.setValue(QRCodeUtil.generateHash(qrCodeContent));
    }

    /**
     * Called when user has reviewed the QR Code details and wants to add to account
     *
     * @param playerId The player that's scanning the qr code
     */
    public void completeScan(String playerId) {
        QRCode newQRCode = new QRCode("", qrCodeHash.getValue(), location.getValue(), new ArrayList<>(), new ArrayList<String>() {
            {
                add(playerId);
            }
        });

        qrCodeRepository.addQRCodeToPlayer(newQRCode, playerId);
    }

    /**
     * turns the bitmap to string for storing purpose
     */
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    /**
     * Returns a LiveData object containing the QR code hash value.
     *
     * @return A LiveData object containing the QR code hash value.
     */
    public LiveData<String> getQRCodeHash() {
        return this.qrCodeHash;
    }

    /**
     * Clears the qr code in state
     */
    public void clearQRCode() {
        qrCodeContent.setValue("");
        qrCodeHash.setValue("");
    }
    /**
     * Sets the geolocation coordinates for the current location.
     *
     * @param latitude The latitude coordinate for the current location.
     * @param longitude The longitude coordinate for the current location.
     */
    public void setGeolocation(double latitude, double longitude) {
        Location currentLocation = this.location.getValue();
        currentLocation.latitude = latitude;
        currentLocation.longitude = longitude;

        location.setValue(currentLocation);
    }
    /**
     * Sets the photo location for the current location.
     *
     * @param photo The Bitmap photo to add to the current location. If null, clears the photo location.
     */
    public void setPhotoLocation(Bitmap photo) {
        // Clears photo location if provided with null bitmap
        //  otherwise adds the photo to the location
        Location currentLocation = this.location.getValue();
        if (photo == null) {
            currentLocation.photos = new ArrayList<>();
        }
        else{
            currentLocation.photos.add(BitMapToString(photo));
        }
        location.setValue(currentLocation);
    }

    /**
     * Returns the LiveData object containing the current location.
     *
     * @return The LiveData object containing the current location.
     */
    public LiveData<Location> getLocation() {
        return location;
    }

}