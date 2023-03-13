package com.example.qrhunter.ui.scan;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.ui.scan.ScanViewModel;


import android.graphics.Bitmap;

import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.qrhunter.data.model.Location;
import com.example.qrhunter.data.repository.QRCodeRepository;
import com.example.qrhunter.utils.QRCodeUtil;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ScanViewModelTest{


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    private ScanViewModel scanViewModel;

    @Mock
    private QRCodeRepository qrCodeRepository;

    @Mock
    private Observer<String> qrCodeContentObserver;

    @Mock
    private Observer<String> qrCodeHashObserver;

    @Mock
    private Observer<Location> locationObserver;

    @BeforeEach
    public void setup() {
        scanViewModel = new ScanViewModel();
        qrCodeRepository = new QRCodeRepository();
        scanViewModel.getQRCodeContent().observeForever(qrCodeContentObserver);
        scanViewModel.getQRCodeHash().observeForever(qrCodeHashObserver);
        scanViewModel.getLocation().observeForever(locationObserver);
    }

    @Test
    public void scanQRCode_updatesQRCodeContentAndHash() {
        String qrCodeContent = "Hello, World!";
        String expectedHash = QRCodeUtil.generateHash(qrCodeContent);
        scanViewModel.scanQRCode(qrCodeContent);
        verify(qrCodeContentObserver).onChanged(qrCodeContent);
        verify(qrCodeHashObserver).onChanged(anyString());
    }

    @Test
    public void completeScan_addsNewQRCodeToPlayer() {
        String playerId = "player1";
        String qrCodeHash = "hash123";
        Location location = new Location(0, 0, new ArrayList<>());
        ArrayList<String> playerIds = new ArrayList<String>();
        playerIds.add(playerId);
        QRCode expectedQRCode = new QRCode("", qrCodeHash, location, new ArrayList<>(), playerIds);

        scanViewModel.scanQRCode(qrCodeHash);
        scanViewModel.completeScan(playerId);

        verify(qrCodeRepository).addQRCodeToPlayer(expectedQRCode, playerId);
    }

    @Test
    public void clearQRCode_resetsQRCodeContentAndHash() {
        scanViewModel.clearQRCode();
        verify(qrCodeContentObserver).onChanged("");
        verify(qrCodeHashObserver).onChanged("");
    }

    @Test
    public void setGeolocation_updatesLocation() {
        double latitude = 37.7749;
        double longitude = -122.4194;
        Location expectedLocation = new Location(latitude, longitude, new ArrayList<>());

        scanViewModel.setGeolocation(latitude, longitude);

        verify(locationObserver).onChanged(expectedLocation);
    }

    @Test
    public void addPhotoLocation_updatesLocation() {
        Bitmap photo = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        String photoString = scanViewModel.BitMapToString(photo);
        Location expectedLocation = new Location(0, 0, new ArrayList<>());
        expectedLocation.photos.add(photoString);

        scanViewModel.addPhotoLocation(photo);

        verify(locationObserver).onChanged(expectedLocation);
    }

    @Test
    public void addPhotoLocation_addsPhotoToLocationPhotos() {
        ScanViewModel viewModel = new ScanViewModel();
        Bitmap photo = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        viewModel.addPhotoLocation(photo);
        List<String> photos = viewModel.getLocation().getValue().getPhotos();
        assertNotNull(photos);
        assertEquals(1, photos.size());
        assertEquals(viewModel.BitMapToString(photo), photos.get(0));
    }

    @Test
    public void addPhotoLocation_addsMultiplePhotosToLocationPhotos() {
        ScanViewModel viewModel = new ScanViewModel();
        Bitmap photo1 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Bitmap photo2 = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        viewModel.addPhotoLocation(photo1);
        viewModel.addPhotoLocation(photo2);
        ArrayList<String> photos = viewModel.getLocation().getValue().getPhotos();
        assertNotNull(photos);
        assertEquals(2, photos.size());
        assertEquals(viewModel.BitMapToString(photo1), photos.get(0));
        assertEquals(viewModel.BitMapToString(photo2), photos.get(1));
    }

    @Test
    public void clearPhotoLocation_removesAllPhotosFromLocationPhotos() {
        ScanViewModel viewModel = new ScanViewModel();
        Bitmap photo1 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Bitmap photo2 = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        viewModel.addPhotoLocation(photo1);
        viewModel.addPhotoLocation(photo2);
        viewModel.clearPhotoLocation();
        ArrayList<String> photos = viewModel.getLocation().getValue().getPhotos();
        assertNotNull(photos);
        assertTrue(photos.isEmpty());
    }

    @Test
    public void getLocation_returnsMutableLiveDataOfLocation() {
        ScanViewModel viewModel = new ScanViewModel();
        assertNotNull(viewModel.getLocation());
        assertTrue(viewModel.getLocation() instanceof MutableLiveData<?>);
        assertTrue(viewModel.getLocation().getValue() instanceof Location);
    }
}