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
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test for LoginActivity
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testLoginActivityLaunches() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, LoginActivity.class);
        
        try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(intent)) {
            // Verify login screen elements are displayed
            onView(withId(R.id.loginEmail)).check(matches(isDisplayed()));
            onView(withId(R.id.loginPassword)).check(matches(isDisplayed()));
            onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testLoginButtonIsVisible() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, LoginActivity.class);
        
        try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testGoogleSignInButtonIsVisible() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, LoginActivity.class);
        
        try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.googleSignInButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testSignupRedirectTextIsVisible() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, LoginActivity.class);
        
        try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.signupRedirectText)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testEmailInputAcceptsText() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, LoginActivity.class);
        
        try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.loginEmail))
                .perform(typeText("test@example.com"), closeSoftKeyboard());
        }
    }

    @Test
    public void testPasswordInputAcceptsText() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, LoginActivity.class);
        
        try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.loginPassword))
                .perform(typeText("password123"), closeSoftKeyboard());
        }
    }

    @Test
    public void testLoginFormCanBeFilled() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, LoginActivity.class);
        
        try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(intent)) {
            // Fill in email
            onView(withId(R.id.loginEmail))
                .perform(typeText("test@example.com"), closeSoftKeyboard());
            
            // Fill in password
            onView(withId(R.id.loginPassword))
                .perform(typeText("password123"), closeSoftKeyboard());
            
            // Verify login button is still displayed
            onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        }
    }
}
