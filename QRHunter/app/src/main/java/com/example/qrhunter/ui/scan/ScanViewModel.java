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
import com.example.qrhunter.data.repository.RepositoryCallback;
import com.example.qrhunter.utils.QRCodeUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * The ScanViewModel class is responsible for handling the logic and data for the scan fragment.
 */

public class ScanViewModel extends ViewModel {
    private final MutableLiveData<String> qrCodeContent = new MutableLiveData<>();
    private final MutableLiveData<String> qrCodeHash = new MutableLiveData<>();
    private final MutableLiveData<Location> location = new MutableLiveData<>(new Location(0, 0));
    private final MutableLiveData<Bitmap> photo = new MutableLiveData<>(null);
    private final MutableLiveData<String> scanMessage = new MutableLiveData<>("");
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
     * @param playerId           The player that's scanning the qr code
     * @param savedPhoto         The bytes of the photo location, could be null
     * @param repositoryCallback The repository callback that has the scan message. Ex: "QR Code already scanned".
     *                           This is a quick hack to display a toast from a method in the repository class
     */
    public void completeScan(String qrCodeId, String playerId, byte[] savedPhoto, RepositoryCallback<String> repositoryCallback) {
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

        qrCodeRepository.addQRCodeToPlayer(newQRCode, playerId, savedPhoto, repositoryCallback);
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
    /**
     * Sets the geolocation using math logic
     * @param latitude The latitude that needs to be corrected
     * @param longitude The longitude that needs to be corrected
     */
    public void setGeolocation(double latitude, double longitude) {
        Location currentLocation = this.location.getValue();
        currentLocation.latitude = Math.round(latitude * 10000d) / 10000d;
        currentLocation.longitude = Math.round(longitude * 10000d) / 10000d;

        location.setValue(currentLocation);
    }
    /**
     * Gets the geolocation of the qrCode
     * @return The location of the qrCode
     */
    public LiveData<Location> getLocation() {
        return location;
    }

    /**
     * Gets the photo of the qrCode
     * @return The photo of the qrCode
     */
    public LiveData<Bitmap> getPhoto() {
        return photo;
    }

    /**
     * Sets the photo of the qrCode
     * @return The photo of the qrCode
     */
    public void setPhoto(Bitmap photo) {
        this.photo.setValue(photo);
    }

    /**
     * Gets the scanMessage of the qrCode
     * @return The scanMessage
     */
    public LiveData<String> getScanMessage() {
        return scanMessage;
    }
}