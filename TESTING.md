# Care Crew - Testing Guide

This document provides an overview of the testing approach for the Care Crew Android application.

## Test Coverage

The Care Crew project includes comprehensive unit and UI tests for critical components:

### Unit Tests

Unit tests are located in `app/src/test/java/com/societal/carecrew/` and cover the following model classes:

#### 1. **HelperClassTest**
Tests the main user model class with the following coverage:
- Constructor initialization and default values
- All getter and setter methods
- Skills, interests, and causes management
- Availability management
- Volunteer experience tracking
- Social links management
- Firebase no-argument constructor

#### 2. **GroupTest**
Tests the Group model class:
- Group creation with ID, name, description, and creator
- Member management (add/remove members)
- Null and empty members handling
- Firebase no-argument constructor

#### 3. **PostTest**
Tests the Post model class:
- Post creation with user ID, username, caption, image URL, and post ID
- All getter and setter methods
- Posts with empty captions or without images
- Firebase no-argument constructor

#### 4. **OpportunityTest**
Tests the Opportunity model class (uses Robolectric for Android-specific features):
- Opportunity creation with title, description, date, and location
- Parcelable implementation for passing between activities
- Category field testing
- Empty string handling

#### 5. **VolunteerExperienceTest**
Tests the VolunteerExperience model class:
- Experience creation with organization, role, duration, and description
- All getter and setter methods
- Experience updates
- Empty string handling

#### 6. **AvailabilityTest**
Tests the Availability model class:
- Availability settings for weekdays/weekends
- Time slot availability (mornings, afternoons, evenings)
- Various availability combinations
- All getter and setter methods

### UI/Instrumented Tests

UI tests are located in `app/src/androidTest/java/com/societal/carecrew/` and cover critical user-facing activities:

#### 1. **LoginActivityTest**
Tests the login screen functionality:
- Activity launches successfully
- All UI elements are displayed (email, password, login button, Google sign-in button)
- Email and password fields accept text input
- Complete login form can be filled
- Signup redirect text is visible

#### 2. **SignupActivityTest**
Tests the signup screen functionality:
- Activity launches successfully
- All UI elements are displayed (name, email, username, password, signup button)
- All input fields accept text
- Complete signup form can be filled
- Login redirect text is visible

#### 3. **HomePageActivityTest**
Tests the main home page:
- Activity launches successfully
- Post RecyclerView is displayed
- Create post button is visible
- Navigation view is displayed

## Running the Tests

### Prerequisites
- Android Studio installed
- JDK 17 or higher
- Android SDK configured
- Gradle wrapper configured

### Running Unit Tests

You can run unit tests in several ways:

1. **From Android Studio:**
   - Right-click on the `test` directory
   - Select "Run 'All Tests'"

2. **From Command Line:**
   ```bash
   ./gradlew test
   ```

3. **Run specific test class:**
   ```bash
   ./gradlew test --tests HelperClassTest
   ```

### Running UI Tests

UI tests require an Android device or emulator:

1. **From Android Studio:**
   - Connect a device or start an emulator
   - Right-click on the `androidTest` directory
   - Select "Run 'All Tests'"

2. **From Command Line:**
   ```bash
   ./gradlew connectedAndroidTest
   ```

3. **Run specific UI test:**
   ```bash
   ./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.societal.carecrew.LoginActivityTest
   ```

## Test Dependencies

The project uses the following testing libraries:

- **JUnit 4.13.2**: Core testing framework for unit tests
- **Robolectric 4.11.1**: Android unit testing framework for testing Android-specific code without an emulator
- **AndroidX Test JUnit 1.1.5**: Extensions for instrumented tests
- **Espresso Core 3.5.1**: UI testing framework for Android
- **Espresso Intents 3.5.1**: Intent validation and stubbing for UI tests

## Testing Best Practices

1. **Keep tests focused**: Each test should verify one specific behavior
2. **Use descriptive names**: Test method names clearly describe what is being tested
3. **Arrange-Act-Assert**: Follow the AAA pattern for test structure
4. **Test edge cases**: Include tests for empty strings, null values, and boundary conditions
5. **Maintain test independence**: Tests should not depend on each other

## Future Test Enhancements

Consider adding the following test coverage in the future:

- Integration tests with Firebase (requires test database configuration)
- Network request mocking for API tests
- Performance tests for RecyclerView scrolling
- Screenshot tests for UI regression
- Accessibility tests for screen readers
- End-to-end user flow tests
- Code coverage reports

## Continuous Integration

For CI/CD pipelines, configure your workflow to:

1. Run unit tests on every pull request
2. Run UI tests on merge to main branch
3. Generate and publish test coverage reports
4. Fail builds if tests don't pass

Example GitHub Actions workflow:
```yaml
- name: Run Unit Tests
  run: ./gradlew test

- name: Run UI Tests
  run: ./gradlew connectedAndroidTest
```

## Troubleshooting

### Common Issues

1. **Firebase initialization errors in tests**: 
   - Mock Firebase dependencies for unit tests
   - Use test Firebase configuration for instrumented tests

2. **Network connectivity issues**:
   - Ensure internet access for dependency downloads
   - Configure offline mode if needed

3. **Emulator not starting**:
   - Check AVD configuration
   - Ensure sufficient system resources

## Contributing Tests

When adding new features:
1. Write tests before or alongside the implementation (TDD)
2. Ensure all new code has adequate test coverage
3. Run all tests before submitting pull requests
4. Update this document if adding new test categories

---

For questions or issues related to testing, please open an issue on the GitHub repository.
