package com.example.qrhunter.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.data.repository.QRCodeRepository;

import java.util.List;
/**
 * Model for MapFragment. Contains QRCodes displayed on the map.
 */
public class MapViewModel extends ViewModel {
    private QRCodeRepository qrCodeRepository;
    private LiveData<List<QRCode>> qrCodes;
    public MapViewModel() {
        qrCodeRepository = new QRCodeRepository();
        qrCodes = qrCodeRepository.getQRCodeList();
    }
    /**
     * Gets the qrCodes in the Map
     * @return The qrCodes in the Map
     */
    public LiveData<List<QRCode>> getQRCodes() {
        return qrCodes;
    }

}