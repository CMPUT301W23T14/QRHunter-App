package com.example.qrhunter;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.data.repository.PlayerRepository;
import com.example.qrhunter.data.repository.RepositoryCallback;
import com.example.qrhunter.ui.leaderboard.LeaderboardViewModel;
import com.example.qrhunter.ui.profile.ProfileFragmentDirections;
import com.example.qrhunter.ui.scan.AfterScanFragment;
import com.example.qrhunter.ui.scan.ScanViewModel;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.qrhunter.data.model.Location;
import com.example.qrhunter.data.repository.QRCodeRepository;
import com.example.qrhunter.utils.QRCodeUtil;
import com.example.qrhunter.utils.QRCodeVisual;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(AndroidJUnit4.class)
public class AfterScanFragmentTest {
    private Solo solo;
    private QRCodeRepository qrCodeRepository;
    private PlayerRepository playerRepository;
    private ScanViewModel scanViewModel;
    private MutableLiveData<List<QRCode>> QRCodesLiveData;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        // mock the view model and the repository\
        scanViewModel = Mockito.mock(ScanViewModel.class);
        qrCodeRepository = Mockito.mock(QRCodeRepository.class);
        playerRepository = Mockito.mock(PlayerRepository.class);
        List<Player> dummyData = new ArrayList<>();
        dummyData.add(new Player("fsdf", "John Doe", "1234567890",100));
        String playerId1 = "456";
        QRCode newQRCode = new QRCode("123", "hashValue", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<String>() {{
            add(playerId1);
        }});
        QRCodesLiveData = new MutableLiveData<>();
        MutableLiveData<String> qrCodeHash = new MutableLiveData<>();
        scanViewModel.setQRCodeRepository(qrCodeRepository);
        scanViewModel.setPlayerRepository(playerRepository);
        LiveData<List<Player>> mockedLiveData = Mockito.mock(LiveData.class);
        Mockito.when(mockedLiveData.getValue()).thenReturn(dummyData);
        Mockito.when(playerRepository.getUsers()).thenReturn(mockedLiveData);
        Mockito.when(qrCodeRepository.getQRCodeList()).thenReturn(QRCodesLiveData);
        Mockito.when(scanViewModel.getQRCodeHash()).thenReturn(qrCodeHash);
        Mockito.when(scanViewModel.getPhoto()).thenReturn(null);


        activityScenarioRule.getScenario().onActivity(activity -> {
            scanViewModel = new ViewModelProvider(activity).get(ScanViewModel.class);
            scanViewModel.setQRCodeRepository(qrCodeRepository);
            scanViewModel.setPlayerRepository(playerRepository);
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
            navController.navigate(R.id.navigation_scan);
            scanViewModel.scanQRCode("12345678");
            scanViewModel.setGeolocation(0.01, 0.02);
            navController.navigate(com.example.qrhunter.ui.scan.ScanFragmentDirections.actionScanFragmentToAfterScanFragment());
        });

        // add delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Player testPlayer1 = new Player("fsdf", "John Doe", "1234567890",100);
        db.collection("players").document("TestPlayer").set(testPlayer1);

        /*

        db.collection("qrCodes").document("TestQRCode").set(testQRCode1);
         */

    }

    @Test
    public void checkForCorrectName() {
        //solo.clickOnView(solo.getView(R.id.scanButton));
        String testHash = QRCodeUtil.generateHash("12345678");
        String testName = QRCodeUtil.generateName(testHash);
        onView(withId(R.id.QR_name)).check(matches(withText(testName)));
    }

    /**
     * This test checks that the correct name is displayed on the QR code fragment.
     * It generates a hash string and name string for test data, launches the QR code fragment,
     * and checks that the name displayed on the fragment matches the generated name.
     */
    @Test
    public void checkForCorrectVisual() {
        //solo.clickOnView(solo.getView(R.id.scanButton));
        ArrayList<Location> locations = new ArrayList<>();
        Location location = new Location(37.7749, -122.4194);
        locations.add(location);
        ArrayList<String> playerId = new ArrayList<>();
        Player testPlayer1 = new Player("fsdf", "John Doe", "1234567890",100);
        playerId.add(testPlayer1.getId());
        QRCode testQRCode1 = new QRCode("1234", "abcdefg", locations, new ArrayList<>(), new ArrayList<>(), playerId);
        onView(withId(R.id.QR_name));
    }

    /**
     * Test method to check if the correct score is displayed for a QR code content.
     * Generates a test hash from a sample QR code content, creates a QRCode object from it,
     * and checks if the score displayed for the QR code matches the expected score generated
     */
    @Test
    public void checkForCorrectScore() {
        String qrCodeContent = "12345678";
        String expectedHash = QRCodeUtil.generateHash(qrCodeContent);
        QRCode qrCode = new QRCode("1", expectedHash, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        List<QRCode> qrCodesList = new ArrayList<>();
        qrCodesList.add(qrCode);
        String expectedScore = QRCodeUtil.generateScore(expectedHash) + " Points";
        onView(withId(R.id.QRCodeScoretext)).check(matches(withText(expectedScore)));
    }

    /**
     * This test checks that the user can add multiple geo-locations to a QR code.
     * It generates test data for a single QR code with no geo-locations, launches the QR code fragment,
     * and clicks the "Add Geo-Location" button twice to add two geo-locations to the QR code.
     */
    @Test
    public void checkForCorrectLocation() {
        String qrCodeContent = "Hello, World!";
        String expectedHash = QRCodeUtil.generateHash(qrCodeContent);
        QRCode qrCode = new QRCode("1", expectedHash, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        List<QRCode> qrCodesList = new ArrayList<>();
        qrCodesList.add(qrCode);
        onView(withId(R.id.addGeoLocationButton)).perform(click());
        onView(withId(R.id.addGeoLocationButton)).perform(click());
    }

    /**
     * This test checks the complete scanning functionality by creating a new QRCode object,
     * mocking the addQRCodeToPlayer method of the QRCodeRepository,
     * and calling the completeScan method of the ScanViewModel.
     * It verifies that the save_button is clicked and the onComplete method of the repositoryCallback
     */
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
        onView(withId(R.id.addGeoLocationButton)).perform(click());
        scanViewModel.completeScan(qrCodeId, playerId, null, repositoryCallback);
        onView(withId(R.id.save_button)).perform(click());
    }


    @After
    public void tearDown() throws Exception {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("players").document("TestPlayer").delete();
    }
}
