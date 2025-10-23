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
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test for SignupActivity
 */
@RunWith(AndroidJUnit4.class)
public class SignupActivityTest {

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testSignupActivityLaunches() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, SignupActivity.class);
        
        try (ActivityScenario<SignupActivity> scenario = ActivityScenario.launch(intent)) {
            // Verify signup screen elements are displayed
            onView(withId(R.id.signupName)).check(matches(isDisplayed()));
            onView(withId(R.id.signupEmail)).check(matches(isDisplayed()));
            onView(withId(R.id.signupUsername)).check(matches(isDisplayed()));
            onView(withId(R.id.signupPassword)).check(matches(isDisplayed()));
            onView(withId(R.id.signupButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testSignupButtonIsVisible() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, SignupActivity.class);
        
        try (ActivityScenario<SignupActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.signupButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testLoginRedirectTextIsVisible() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, SignupActivity.class);
        
        try (ActivityScenario<SignupActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.loginRedirectText)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testNameInputAcceptsText() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, SignupActivity.class);
        
        try (ActivityScenario<SignupActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.signupName))
                .perform(typeText("John Doe"), closeSoftKeyboard());
        }
    }

    @Test
    public void testEmailInputAcceptsText() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, SignupActivity.class);
        
        try (ActivityScenario<SignupActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.signupEmail))
                .perform(typeText("john@example.com"), closeSoftKeyboard());
        }
    }

    @Test
    public void testUsernameInputAcceptsText() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, SignupActivity.class);
        
        try (ActivityScenario<SignupActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.signupUsername))
                .perform(typeText("johndoe"), closeSoftKeyboard());
        }
    }

    @Test
    public void testPasswordInputAcceptsText() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, SignupActivity.class);
        
        try (ActivityScenario<SignupActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.signupPassword))
                .perform(typeText("password123"), closeSoftKeyboard());
        }
    }

    @Test
    public void testSignupFormCanBeFilled() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, SignupActivity.class);
        
        try (ActivityScenario<SignupActivity> scenario = ActivityScenario.launch(intent)) {
            // Fill in all fields
            onView(withId(R.id.signupName))
                .perform(typeText("John Doe"), closeSoftKeyboard());
            
            onView(withId(R.id.signupEmail))
                .perform(typeText("john@example.com"), closeSoftKeyboard());
            
            onView(withId(R.id.signupUsername))
                .perform(typeText("johndoe"), closeSoftKeyboard());
            
            onView(withId(R.id.signupPassword))
                .perform(typeText("password123"), closeSoftKeyboard());
            
            // Verify signup button is still displayed
            onView(withId(R.id.signupButton)).check(matches(isDisplayed()));
        }
    }
}
