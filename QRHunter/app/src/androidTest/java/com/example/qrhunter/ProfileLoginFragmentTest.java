package com.example.qrhunter;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
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

/**
 * Intent test for profileInit fragment
 *
 */
public class ProfileLoginFragmentTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);
    private Solo solo;

    /**
     * Runs before all tests and sets up for test environment
     *
     * @throws Exception
     */
    @Before
    public void setup() {
        // Launch the profile fragment
        activityScenarioRule.getScenario().onActivity(activity -> {
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
            navController.navigate(R.id.playerInitFragment);
        });
        // add delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * Tests to see if test string input works
     *
     */
    @Test
    public void add_username(){
        String test_var = "test";
        // Type and enter test string
        onView(withId(R.id.username_edit_text)).perform(typeText(test_var));
        // Check whether the test string was inputted properly
        onView(withId(R.id.username_edit_text)).check(matches(withText(test_var)));
    }

    /**
     * Tests to see if no input works
     *
     */
    @Test
    public void add_nothing(){
        // Check nothing case
        onView(withId(R.id.username_edit_text)).perform(typeText(""));
        // Check whether the empty string did not input anything
        onView(withId(R.id.username_edit_text)).check(matches(withText("")));
        try {
            onView(withText("Next")).perform(click());
        }
        catch (Exception e){

        }
    }
}
