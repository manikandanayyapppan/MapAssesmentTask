plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.mapassesmenttask"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mapassesmenttask"
        minSdk = 24
        targetSdk = 35
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
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")
    implementation ("com.github.bumptech.glide:glide:4.13.2")
    implementation ("io.insert-koin:koin-core:3.2.2")
    implementation ("io.insert-koin:koin-android:3.2.3")
    implementation ("io.insert-koin:koin-ktor:3.2.2")
    implementation ("io.insert-koin:koin-android:3.4.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation ("androidx.browser:browser:1.3.0")
    implementation ("androidx.datastore:datastore-preferences:1.0.0")

    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)


    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.4.0")
}