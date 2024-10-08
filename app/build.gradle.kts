plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.webviewapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.webviewapplication"
        minSdk = 24
        targetSdk = 34
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    packagingOptions {
          resources.excludes.add("META-INF/DEPENDENCIES")
          resources.excludes.add("META-INF/LICENSE")
          resources.excludes.add("META-INF/LICENSE.txt")
          resources.excludes.add("META-INF/license.txt")
          resources.excludes.add("META-INF/NOTICE")
          resources.excludes.add("META-INF/NOTICE.txt")
          resources.excludes.add("META-INF/notice.txt")
          resources.excludes.add("META-INF/ASL2.0")
          resources.excludes.add("META-INF/*.kotlin_module")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("com.google.android.play:review:2.0.1")
    implementation("com.google.android.play:review-ktx:2.0.1")
    implementation ("com.google.android.recaptcha:recaptcha:18.2.1")

    implementation ("com.google.api-client:google-api-client-android:1.33.0") // For Google API client
    implementation ("com.google.auth:google-auth-library-oauth2-http:1.10.0") // For Google Auth library
//    implementation ("com.google.code.gson:gson:2.10") // For JSON parsing
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // For coroutines

    //bio-metric
    implementation ("androidx.biometric:biometric:1.2.0-alpha01")


}