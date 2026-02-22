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

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

/**
 * Instrumented test for HomePageActivity.
 * HomePageActivity redirects unauthenticated users to LoginActivity in onStart(),
 * so tests verify this redirect behavior rather than authenticated-only UI elements.
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
    public void testUnauthenticatedUserRedirectsToLogin() {
        // In a test environment FirebaseAuth.getCurrentUser() returns null,
        // so HomePageActivity.onStart() immediately starts LoginActivity.
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, HomePageActivity.class);

        try (ActivityScenario<HomePageActivity> scenario = ActivityScenario.launch(intent)) {
            intended(hasComponent(LoginActivity.class.getName()));
        }
    }
}
