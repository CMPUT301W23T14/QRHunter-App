package com.example.qrhunter;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.repository.PlayerRepository;
import com.example.qrhunter.ui.leaderboard.LeaderboardViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

//@RunWith(AndroidJUnit4.class)
@RunWith(MockitoJUnitRunner.class)
public class LeaderboardFragmentTest {

    //    @Rule
//    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);


    @Before
    public void setUp() {
        // Mock the PlayerRepository so that it returns some dummy data
        PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
        LeaderboardViewModel leaderboardViewModel = Mockito.mock(LeaderboardViewModel.class);
        List<Player> dummyData = new ArrayList<>();
        dummyData.add(new Player("fsdf", "John Doe", "1234567890", 100));
        dummyData.add(new Player("fsdf1", "Jane Doe", "1234567890", 90));
        dummyData.add(new Player("fsdf2", "John Smith", "1234567890", 80));
        dummyData.add(new Player("fsd43f", "Jane Smith", "1234567890", 70));
        dummyData.add(new Player("fsd5f", "John Doe", "1234567890", 60));


        leaderboardViewModel.setPlayerRepository(playerRepository);

        LiveData<List<Player>> mockedLiveData = Mockito.mock(LiveData.class);
        Mockito.when(mockedLiveData.getValue()).thenReturn(dummyData);
        Mockito.when(playerRepository.getUsers()).thenReturn(mockedLiveData);
        Mockito.when(leaderboardViewModel.updatePlayers()).thenReturn(mockedLiveData);
        Mockito.when(leaderboardViewModel.getPlayers()).thenReturn(mockedLiveData);


        activityScenarioRule.getScenario().onActivity(activity -> {
            leaderboardViewModel.setPlayerRepository(playerRepository);
        });
//        // Disable animations before each test
//        activityScenarioRule.getScenario().onActivity(activity -> {
//            // Disable animations
//            activity.getWindow().setWindowAnimations(android.R.style.Animation);
//        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Player player = new Player("fsdwergibhliuhgrf", "TestingWinner", "1234567890", 99999999);
        db.collection("players").document("TestPlayer").set(player);
    }

    /**
     * Tests that the leaderboard fragment is displayed when you navigate to it
     */
    @Test
    public void testNavigation(){
        // Launch the leaderboard fragment
        activityScenarioRule.getScenario().onActivity(activity -> {
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
            navController.navigate(R.id.navigation_leaderboard);
        });
        // add delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Wait for the leaderboard fragment to be displayed
        onView(withId(R.id.leaderboard_list_view)).check(matches(isDisplayed()));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("players").document("TestPlayer").delete();
    }


    /**
     * Tests that the you navigate to the profile fragment when you click on a list item
     */
    @Test
    public void testClickOnListItem_navigatesToProfileFragment() {
        // Launch the leaderboard fragment
        activityScenarioRule.getScenario().onActivity(activity -> {
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
            navController.navigate(R.id.navigation_leaderboard);
        });
        // add delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Wait for the leaderboard fragment to be displayed
        onView(withId(R.id.leaderboard_list_view)).check(matches(isDisplayed()));


        // Click on the first item in the list
//        onView(withId(R.id.leaderboard_list_view)).perform(click());
        onData(anything())
                .inAdapterView(withId(R.id.leaderboard_list_view))
                .atPosition(0)
                .perform(click());


        // Check that the navigation to the profile fragment occurred
        onView(withId(R.id.navigation_profile)).check(matches(isDisplayed()));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.username)).check(matches(withText("TestingWinner")));

        // Delete the test player
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("players").document("TestPlayer").delete();


    }

    @Test
    public void testRefreshButton() {
        // Launch the leaderboard fragment
        activityScenarioRule.getScenario().onActivity(activity -> {
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
            navController.navigate(R.id.navigation_leaderboard);
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Player player = new Player("fsdwergibhliuhgrf", "TestingWinner2", "1234567890", 99999999);
        db.collection("players").document("TestPlayer").set(player);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // now press the refresh button he should appear on top
        onView(withId(R.id.leaderboard_refresh_button)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onData(anything())
                .inAdapterView(withId(R.id.leaderboard_list_view))
                .atPosition(0)
                .check((matches(hasDescendant(withText("TestingWinner2")))));


        // Wait for the leaderboard fragment to be displayed
        onView(withId(R.id.leaderboard_list_view)).check(matches(isDisplayed()));


        // Delete the test player
        db.collection("players").document("TestPlayer").delete();

        // Click on the first item in the list
    }


    @Test
    public void testSearchView() {
        // Launch the leaderboard fragment
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        PlayerRepository playerRepository = new PlayerRepository();
        Player player = new Player("fsdwergibhliuhgrf", "Rr0zlcsK1JDDSDd@Q9#*kLT6TS8aPGLBmjU2iv@mvE8eTmXqkg", "1234567890", 100);
        db.collection("players").document("TestPlayer").set(player);


        activityScenarioRule.getScenario().onActivity(activity -> {
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
            navController.navigate(R.id.navigation_leaderboard);
        });

        // Wait for the leaderboard fragment to be displayed
        onView(withId(R.id.leaderboard_list_view)).check(matches(isDisplayed()));

        // Click on the first item in the list
//        onView(withId(R.id.leaderboard_search_user)).perform(click());

        // Get the SearchView view
        ViewInteraction searchViewInteraction = onView(withId(R.id.leaderboard_search_user));

        // Click on the left side of the SearchView to expand it
        searchViewInteraction.perform(new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {
                        // Calculate the coordinates of the left side of the SearchView
                        int[] locationOnScreen = new int[2];
                        view.getLocationOnScreen(locationOnScreen);
                        int x = locationOnScreen[0] + 16; // Adjust this value to match the size of your search icon
                        int y = locationOnScreen[1] + view.getHeight() / 2;
                        return new float[]{x, y};
                    }
                },
                Press.FINGER
        ));


        onView(withId(R.id.leaderboard_search_user))
                .perform(typeText("Rr0zlcsK1JDDSDd@Q9#*kLT6TS8aPGLBmjU2iv@mvE8eTmXqkg"));
        // Check that the navigation to the profile fragment occurred
//        onView(withId(R.id.navigation_profile)).check(matches(isDisplayed()));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onData(anything())
                .inAdapterView(withId(R.id.leaderboard_list_view))
                .atPosition(0)
                .check(matches(hasDescendant(withText("Rr0zlcsK1JDDSDd@Q9#*kLT6TS8aPGLBmjU2iv@mvE8eTmXqkg"))));

        db.collection("players").document("TestPlayer").delete();

    }








//    @After
//    public void tearDown() {
//        // Enable animations after each test
//        activityScenarioRule.getScenario().onActivity(activity -> {
//            // Enable animations
//            activity.getWindow().setWindowAnimations(android.R.style.Animation_Activity);
//        });
//    }

}
