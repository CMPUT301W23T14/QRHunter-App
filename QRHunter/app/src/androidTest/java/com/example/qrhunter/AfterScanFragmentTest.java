package com.example.qrhunter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.data.repository.PlayerRepository;
import com.example.qrhunter.data.repository.RepositoryCallback;
import com.example.qrhunter.ui.leaderboard.LeaderboardViewModel;
import com.example.qrhunter.ui.scan.ScanViewModel;


import android.graphics.Bitmap;

import androidx.lifecycle.Observer;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.qrhunter.data.model.Location;
import com.example.qrhunter.data.repository.QRCodeRepository;
import com.example.qrhunter.utils.QRCodeUtil;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class AfterScanFragmentTest {
    private QRCodeRepository qrCodeRepository;
    private PlayerRepository playerRepository;
    private ScanViewModel scanViewModel;
    private MutableLiveData<List<QRCode>> QRCodesLiveData;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);


    @Before
    public void setup() {
        // mock the view model and the repository
        scanViewModel = Mockito.mock(ScanViewModel.class);
        qrCodeRepository = Mockito.mock(QRCodeRepository.class);
        playerRepository = Mockito.mock(PlayerRepository.class);
        List<Player> dummyData = new ArrayList<>();
        dummyData.add(new Player("fsdf", "John Doe", "1234567890",100));
        MutableLiveData<String> qrCodeHash = new MutableLiveData<>();
        String playerId1 = "456";
        QRCode newQRCode = new QRCode("123", "hashValue", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<String>() {{
            add(playerId1);
        }});
        QRCodesLiveData = new MutableLiveData<>();

        scanViewModel.setQRCodeRepository(qrCodeRepository);
        scanViewModel.setPlayerRepository(playerRepository);
        LiveData<List<Player>> mockedLiveData = Mockito.mock(LiveData.class);
        Mockito.when(mockedLiveData.getValue()).thenReturn(dummyData);
        Mockito.when(playerRepository.getUsers()).thenReturn(mockedLiveData);
        Mockito.when(qrCodeRepository.getQRCodeList()).thenReturn(QRCodesLiveData);
        Mockito.when(scanViewModel.getQRCodeHash()).thenReturn(qrCodeHash);
        Mockito.when(scanViewModel.getPhoto()).thenReturn(null);


        activityScenarioRule.getScenario().onActivity(activity -> {
            scanViewModel.setQRCodeRepository(qrCodeRepository);
            scanViewModel.setPlayerRepository(playerRepository);
        });


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Player testPlayer1 = new Player("fsdf", "John Doe", "1234567890",100);
        db.collection("players").document("TestPlayer").set(testPlayer1);


        /*
        ArrayList<Location> locations = new ArrayList<>();
        Location location = new Location(37.7749, -122.4194);
        locations.add(location);
        ArrayList<String> playerId = new ArrayList<>();
        playerId.add(testPlayer1.getId());
        QRCode testQRCode1 = new QRCode("1234", "abcdefg", locations, new ArrayList<>(), new ArrayList<>(), playerId);
        db.collection("qrCodes").document("TestQRCode").set(testQRCode1);
         */

    }

    @Test
    public void testCompleteScan() {
        String qrCodeId = "123";
        String playerId = "456";
        byte[] savedPhoto = null;
        RepositoryCallback<String> repositoryCallback = Mockito.mock(RepositoryCallback.class);
        ArrayList<Location> locations = new ArrayList<>();
        ArrayList<String> photos = new ArrayList<>();

        // Create a new QRCode object
        QRCode qrCode = new QRCode(qrCodeId, "hashValue", locations, photos, new ArrayList<>(), new ArrayList<String>() {{
            add(playerId);
        }});

        // Mock the addQRCodeToPlayer method of the QRCodeRepository
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                RepositoryCallback<String> callback = invocation.getArgument(2);
                // Simulate successful addition of QRCode to player
                callback.onSuccess("");
                return null;
            }
        }).when(qrCodeRepository).addQRCodeToPlayer(eq(qrCode), eq(playerId), eq(null), eq(repositoryCallback));

        // Call the completeScan method
        scanViewModel.completeScan(qrCodeId, playerId, null, repositoryCallback);
    }

    /*
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
        Assert.assertTrue(photos.isEmpty());
    }

    @Test
    public void getLocation_returnsMutableLiveDataOfLocation() {
        ScanViewModel viewModel = new ScanViewModel();
        assertNotNull(viewModel.getLocation());
        Assert.assertTrue(viewModel.getLocation() instanceof MutableLiveData<?>);
        Assert.assertTrue(viewModel.getLocation().getValue() instanceof Location);
    }

     */

}
