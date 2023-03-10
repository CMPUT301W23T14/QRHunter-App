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
    private QRCodeRepository qrCodeRepository = new QRCodeRepository();
    private final MutableLiveData<String> qrCodeHash = new MutableLiveData<>();


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
        QRCodeUtil.generateHash(this.qrCodeContent.toString());
        this.qrCodeHash.setValue(qrCodeContent);
    }

    /**
     * Called when user has reviewed the QR Code details and wants to add to account
     */
    public void createQRCode(String hashValue, Bitmap savedPhoto, int latitude, int longitude) {
        ArrayList<String> photos = new ArrayList<String>();
        photos.add(BitMapToString(savedPhoto));
        Location location = new Location(latitude, longitude, photos);
        QRCode newQRCode = new QRCode(hashValue, location, null);

        // add qrcode to database
        // QRCodeRepository.addQRCode(newQRCode);
    }

    /**
     * turns the bitmap to string for storing purpose
     */
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos =new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public MutableLiveData<String> getQRCodeHash(){
        return this.qrCodeHash;
    }


}