package com.societal.carecrew;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test for HomePageActivity
 * Note: These tests verify UI elements are present. 
 * Full integration tests would require Firebase authentication to be set up.
 */
@RunWith(AndroidJUnit4.class)
public class HomePageActivityTest {

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testHomePageActivityLaunches() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, HomePageActivity.class);
        
        try (ActivityScenario<HomePageActivity> scenario = ActivityScenario.launch(intent)) {
            // Verify key UI elements are displayed
            onView(withId(R.id.postRecyclerView)).check(matches(isDisplayed()));
            onView(withId(R.id.createPostButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testCreatePostButtonIsVisible() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, HomePageActivity.class);
        
        try (ActivityScenario<HomePageActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.createPostButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testNavigationViewIsVisible() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, HomePageActivity.class);
        
        try (ActivityScenario<HomePageActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.navView)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testPostRecyclerViewIsVisible() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, HomePageActivity.class);
        
        try (ActivityScenario<HomePageActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.postRecyclerView)).check(matches(isDisplayed()));
        }
    }
}
