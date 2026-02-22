import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.kotlin.android)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

android {
    namespace = "com.societal.carecrew"
    compileSdk = 35
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    defaultConfig {
        applicationId = "com.societal.carecrew"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val imgBbApiKey = localProperties.getProperty("IMG_BB_API_KEY", "")
        if (imgBbApiKey.isEmpty()) {
            logger.warn("WARNING: IMG_BB_API_KEY is not set in local.properties. Image uploads will not work. See README.md for setup instructions.")
        }
        buildConfigField("String", "IMG_BB_API_KEY", "\"${imgBbApiKey}\"")
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
dependencies {
    // Core AndroidX libraries
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    
    // Firebase libraries
    implementation(platform(libs.firebase.bom))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation(libs.firebase.database)
    // Core AndroidX libraries
    implementation("androidx.core:core-ktx:1.9.0")
    // UI libraries
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // Firebase libraries
    implementation(platform("com.google.firebase:firebase-bom:32.2.3"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:20.6.0")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    // Networking library
    implementation(libs.okhttp)
    // Image loading library
    implementation("com.github.bumptech.glide:glide:4.13.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.0")
    // Other dependencies
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.robolectric:robolectric:4.11.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Maps SDK for Android
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    // Animation dependency
    implementation("com.airbnb.android:lottie:6.1.0")
}