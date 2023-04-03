package com.example.qrhunter;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ProfileFragmentTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);
    private Solo solo;

    @Before
    public void setup() {
        // Launch the profile fragment
        activityScenarioRule.getScenario().onActivity(activity -> {
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
            navController.navigate(R.id.navigation_profile);
        });
        // add delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void add_phone_number(){
        // Type and enter phone number
        onView(withId(R.id.phone_number_edit_text)).perform(typeText("000222333"));
        Espresso.closeSoftKeyboard();
        // Check whether the phone number is the one correctly inputted
        onView(withId(R.id.phone_number_edit_text)).check(matches(withText("000222333")));
    }
}
