package com.example.qrhunter;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.qrhunter.ui.profile.ProfileFragmentDirections;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class QRCodeFragmentTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);
    private Solo solo;

    @Before
    public void setup() {
        // Launch the qr code fragment
        activityScenarioRule.getScenario().onActivity(activity -> {
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
            navController.navigate(R.id.navigation_profile);

            com.example.qrhunter.ui.profile.ProfileFragmentDirections.ActionNavigationProfileToQrCodeFragment action =
                    ProfileFragmentDirections.actionNavigationProfileToQrCodeFragment("123");
            navController.navigate(action);
        });
        // add delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fragment_can_be_instantiated() {
        // Wait for the leaderboard fragment to be displayed
        onView(withId(R.id.QR_name)).check(matches(withText("Name")));
        onView(withId(R.id.QRCodeScoretext)).check(matches(withText("0.0")));
    }

    @Test
    public void adding_comment() {
        // Type and enter comment
        onView(withId(R.id.new_comment_edit_text)).perform(typeText("New comment"));
        onView(withContentDescription("add comment")).perform(click());

        // Check whether comment exist. FloMoMegaSpectralCrab is the default author name
        onView(withText("New comment")).check(matches(isDisplayed()));
    }
}
