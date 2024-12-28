plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}
android {
    namespace = "com.societal.carecrew"
    compileSdk = 34
    buildFeatures {
        viewBinding = true
    }
    defaultConfig {
        applicationId = "com.societal.carecrew"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    dependencies {
// Core AndroidX libraries
        implementation("androidx.core:core-ktx:1.9.0")
// UI libraries
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
// Firebase libraries
        implementation(platform("com.google.firebase:firebase-bom:32.2.3"))
        implementation("com.google.firebase:firebase-auth-ktx")
        implementation("com.google.android.gms:play-services-auth:20.6.0")
        implementation("com.google.firebase:firebase-analytics-ktx")
// Networking library
        implementation(libs.okhttp)
// Image loading library
        implementation("com.github.bumptech.glide:glide:4.13.0")
        annotationProcessor("com.github.bumptech.glide:compiler:4.13.0")
// Other dependencies
        testImplementation("junit:junit:4.13.2") // For unit testing
        androidTestImplementation("androidx.test.ext:junit:1.1.5") // For instrumentation testing
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // For UI testing
// Maps SDK for Android
        implementation("com.google.android.gms:play-services-maps:18.1.0")

        //animation dependency
        implementation("com.airbnb.android:lottie:6.1.0")
    }
}