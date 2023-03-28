package com.example.qrhunter;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.qrhunter.MainActivity;
import com.example.qrhunter.R;
import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.repository.PlayerRepository;
import com.example.qrhunter.ui.leaderboard.LeaderboardViewModel;

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

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);



    @Before
    public void setUp() {
        // Mock the PlayerRepository so that it returns some dummy data
        PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
        LeaderboardViewModel leaderboardViewModel = Mockito.mock(LeaderboardViewModel.class);
        List<Player> dummyData = new ArrayList<>();
//        Player(String id, String username, String phoneNumber, int rank, int totalScore)
        dummyData.add(new Player("fsdf", "John Doe", "1234567890", 1, 100));
        dummyData.add(new Player("fsdf1", "Jane Doe", "1234567890", 2, 90));
        dummyData.add(new Player("fsdf2",   "John Smith", "1234567890", 3, 80));
        dummyData.add(new Player("fsd43f", "Jane Smith", "1234567890", 4, 70));
        dummyData.add(new Player("fsd5f", "John Doe", "1234567890", 5, 60));
//        Mockito.when(playerRepository.getUsers()).thenReturn(new MutableLiveData<>(dummyData));
//        Mockito.when(playerRepository.getUsers()).thenReturn(new LiveData<>(dummyData));

        leaderboardViewModel.setPlayerRepository(playerRepository);

        LiveData<List<Player>> mockedLiveData = Mockito.mock(LiveData.class);
        Mockito.when(mockedLiveData.getValue()).thenReturn(dummyData);
        Mockito.when(playerRepository.getUsers()).thenReturn(mockedLiveData);
        Mockito.when(leaderboardViewModel.updatePlayers()).thenReturn(mockedLiveData);
        Mockito.when(leaderboardViewModel.getPlayers()).thenReturn(mockedLiveData);

        // Replace the real PlayerRepository with the mock one in the ViewModel
//        LeaderboardViewModel leaderboardViewModel = new ViewModelProvider(activityScenarioRule.getScenario().getLifecycle().getCurrentState()).get(LeaderboardViewModel.class);
//        LeaderboardViewModel leaderboardViewModel = new ViewModelProvider(activityScenarioRule.getScenario().getLifecycle()).get(LeaderboardViewModel.class);

        activityScenarioRule.getScenario().onActivity(activity -> {
            leaderboardViewModel.setPlayerRepository(playerRepository);
        });
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
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Wait for the leaderboard fragment to be displayed
        onView(withId(R.id.leaderboard_list_view)).check(matches(isDisplayed()));

        // Click on the first item in the list
        onView(withId(R.id.leaderboard_list_view)).perform(click());

        // Check that the navigation to the profile fragment occurred
        onView(withId(R.id.navigation_profile)).check(matches(isDisplayed()));

    }

    @Test
    public void testSearchView() {
        // Launch the leaderboard fragment
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        activityScenarioRule.getScenario().onActivity(activity -> {
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
            navController.navigate(R.id.navigation_leaderboard);
        });

        // Wait for the leaderboard fragment to be displayed
        onView(withId(R.id.leaderboard_list_view)).check(matches(isDisplayed()));

        // Click on the first item in the list
        onView(withId(R.id.leaderboard_list_view)).perform(click());

        // Check that the navigation to the profile fragment occurred
        onView(withId(R.id.navigation_profile)).check(matches(isDisplayed()));

    }


}
