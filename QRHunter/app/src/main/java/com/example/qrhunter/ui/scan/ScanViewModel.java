package com.example.qrhunter.ui.scan;

import android.graphics.Bitmap;
import android.util.Base64;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrhunter.data.model.Location;
import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.data.repository.PlayerRepository;
import com.example.qrhunter.data.repository.QRCodeRepository;
import com.example.qrhunter.utils.QRCodeUtil;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ScanViewModel extends ViewModel {
    private final MutableLiveData<String> qrCodeContent = new MutableLiveData<>();
    private final MutableLiveData<String> qrCodeHash = new MutableLiveData<>();
    private final MutableLiveData<Location> location = new MutableLiveData<>(new Location(0, 0));
    private final MutableLiveData<Bitmap> photo = new MutableLiveData<>(null);
    private QRCodeRepository qrCodeRepository = new QRCodeRepository();
    private PlayerRepository playerRepository = new PlayerRepository();

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
    public void completeScan(String qrCodeId, String playerId, byte[] savedPhoto) {
        ArrayList<Location> locations = new ArrayList<>();
        ArrayList<String> photos = new ArrayList<>();


        // Don't add to location array if the location.latitude and location.longitude are 0
        if (location.getValue().getLatitude() != 0 || location.getValue().getLongitude() != 0) {
            locations.add(location.getValue());
        }

        QRCode newQRCode = new QRCode(qrCodeId, qrCodeHash.getValue(), locations, photos, new ArrayList<>(), new ArrayList<String>() {
            {
                add(playerId);
            }
        });

        qrCodeRepository.addQRCodeToPlayer(newQRCode, playerId, savedPhoto);
    }

    /**
     * Turns the bitmap of a photo to string for storing purpose
     *
     * @param bitmap The bitmap of a photo
     * @return The converted string
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

    public LiveData<Location> getLocation() {
        return location;
    }

    public LiveData<Bitmap> getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo.setValue(photo);
    }

}