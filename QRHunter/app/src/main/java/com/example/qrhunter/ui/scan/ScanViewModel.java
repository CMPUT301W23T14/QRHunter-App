package com.example.qrhunter.ui.scan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrhunter.data.repository.QRCodeRepository;
import com.example.qrhunter.utils.QRCodeUtil;

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
    public void createQRCode() {
        // qrCodeRepository.addQRCode();
    }

    public MutableLiveData<String> getQRCodeHash(){
        return this.qrCodeHash;
    }

}