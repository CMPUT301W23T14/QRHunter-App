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
     */
    public void createQRCode() {
        QRCode newQRCode = new QRCode(qrCodeHash.getValue(), location.getValue(), null);

        // add qrcode to database
        // QRCodeRepository.addQRCode(newQRCode);
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

    public void setGeolocation(double latitude, double longitude) {
        Location currentLocation = this.location.getValue();
        currentLocation.latitude = latitude;
        currentLocation.longitude = longitude;

        location.setValue(currentLocation);
    }

    public void addPhotoLocation(Bitmap photo) {
        Location currentLocation = this.location.getValue();
        currentLocation.photos.add(BitMapToString(photo));

        location.setValue(currentLocation);
    }

    public void clearPhotoLocation() {
        Location currentLocation = this.location.getValue();
        currentLocation.photos = new ArrayList<>();

        location.setValue(currentLocation);
    }

    public LiveData<Location> getLocation() {
        return location;
    }

}