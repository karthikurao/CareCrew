# Google Maps API Setup Guide

This guide will help you set up the Google Maps API for the Care Crew application.

## Prerequisites

- A Google Cloud Platform (GCP) account
- The Care Crew Android project set up in Android Studio

## Steps to Set Up Google Maps API

### 1. Create a Google Cloud Project

1. Go to the [Google Cloud Console](https://console.cloud.google.com/)
2. Click on "Select a project" at the top
3. Click "New Project"
4. Enter a project name (e.g., "CareCrew")
5. Click "Create"

### 2. Enable the Maps SDK for Android

1. In the Google Cloud Console, navigate to "APIs & Services" > "Library"
2. Search for "Maps SDK for Android"
3. Click on it and then click "Enable"

### 3. Enable the Geocoding API (Optional but Recommended)

1. In the Google Cloud Console, navigate to "APIs & Services" > "Library"
2. Search for "Geocoding API"
3. Click on it and then click "Enable"

This allows the app to convert addresses to coordinates and vice versa.

### 4. Create an API Key

1. In the Google Cloud Console, navigate to "APIs & Services" > "Credentials"
2. Click "Create Credentials" > "API Key"
3. Your new API key will be displayed
4. **Important:** Click "Restrict Key" to add restrictions:
   - Under "Application restrictions", select "Android apps"
   - Click "Add an item" under "Restrict usage to your Android apps"
   - Enter your package name: `com.societal.carecrew`
   - Get your SHA-1 certificate fingerprint:
     ```bash
     # For debug builds
     keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
     
     # For release builds
     keytool -list -v -keystore /path/to/your/release.keystore -alias your-key-alias
     ```
   - Copy the SHA-1 fingerprint and paste it in the GCP console
5. Click "Save"

### 5. Add the API Key to Your Project

1. Open the file `app/src/main/res/values/strings.xml`
2. Find the line:
   ```xml
   <string name="google_maps_key">YOUR_GOOGLE_MAPS_API_KEY</string>
   ```
3. Replace `YOUR_GOOGLE_MAPS_API_KEY` with your actual API key from step 4

**Security Note:** Do not commit your API key to version control. Consider using:
- Android Studio's `local.properties` file (not committed to Git)
- Environment variables
- Google Cloud's API key restrictions to limit usage

### 6. Alternative: Use local.properties (Recommended for Security)

For better security, store your API key in `local.properties`:

1. Open or create `local.properties` in the project root
2. Add the following line:
   ```
   MAPS_API_KEY=your_api_key_here
   ```

3. Update `app/build.gradle.kts` to read from local.properties:
   ```kotlin
   android {
       defaultConfig {
           // ...
           manifestPlaceholders["MAPS_API_KEY"] = 
               project.findProperty("MAPS_API_KEY") ?: "YOUR_GOOGLE_MAPS_API_KEY"
       }
   }
   ```

4. Update `AndroidManifest.xml`:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="${MAPS_API_KEY}" />
   ```

## Testing the Integration

1. Build and run the app on a device or emulator
2. Navigate to the Maps section from the bottom navigation
3. Grant location permissions when prompted
4. You should see:
   - A Google Map displaying your current location
   - Blue markers for volunteers
   - Red markers for community needs/opportunities

## Features

### Map View
- **Real-time Volunteer Locations**: Volunteers are shown as blue markers on the map
- **Community Need Points**: Opportunities/needs are shown as red markers
- **Current Location**: Your current location is highlighted on the map
- **Location Updates**: User locations are automatically updated in Firebase

### Adding Opportunities
- When creating a new opportunity, you can:
  - Use your current location
  - See coordinates displayed
  - Automatically fetch the address from coordinates
  - Save opportunities with precise location data

## Troubleshooting

### Map Shows Only Gray Tiles
- Check that your API key is correct
- Ensure the Maps SDK for Android is enabled in GCP
- Verify the package name and SHA-1 fingerprint are correct in the API key restrictions

### Location Not Working
- Check that location permissions are granted
- Enable location services on your device
- For emulators, set a custom location in the emulator controls

### "This app isn't verified" Error
- This is normal during development
- Click "Advanced" > "Go to [App Name] (unsafe)" to continue
- For production, you'll need to verify your app with Google

## Firebase Database Structure

The app stores location data in Firebase Realtime Database:

```
users/
  {userId}/
    name: "John Doe"
    latitude: 37.7749
    longitude: -122.4194
    ...

opportunities/
  {opportunityId}/
    title: "Food Drive"
    description: "Help distribute food"
    location: "123 Main St"
    latitude: 37.7749
    longitude: -122.4194
    ...
```

## Cost Considerations

Google Maps API has a free tier with generous limits:
- 28,000 map loads per month (free)
- Additional usage is charged

For a small to medium-sized volunteer app, the free tier should be sufficient.

## Additional Resources

- [Google Maps Platform Documentation](https://developers.google.com/maps/documentation)
- [Maps SDK for Android Guide](https://developers.google.com/maps/documentation/android-sdk/overview)
- [Firebase Realtime Database Documentation](https://firebase.google.com/docs/database)
