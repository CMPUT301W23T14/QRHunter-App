package com.example.qrhunter.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.data.repository.QRCodeRepository;

import java.util.List;

public class MapViewModel extends ViewModel {
    private QRCodeRepository qrCodeRepository;
    private LiveData<List<QRCode>> qrCodes;
    public MapViewModel() {
        qrCodeRepository = new QRCodeRepository();
        qrCodes = qrCodeRepository.getQRCodeList();
    }
    public LiveData<List<QRCode>> getQRCodes() {
        return qrCodes;
    }
    public LiveData<List<QRCode>> updateQRCodes() {
        qrCodeRepository = new QRCodeRepository();
        qrCodes = qrCodeRepository.getQRCodeList();
        return qrCodes;
    }
}