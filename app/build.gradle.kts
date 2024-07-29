plugins {
    id("com.android.application")
}

android {
    namespace = "phcom.phlynk.lab5_and103"
    compileSdk = 34

    defaultConfig {
        applicationId = "phcom.phlynk.lab5_and103"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures{
        viewBinding = true;
        dataBinding = true;
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

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.squareup.retrofit2:converter-gson:2.5.0")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.squareup.retrofit2:adapter-rxjava2:latest.version")
    implementation ("com.squareup.retrofit2:retrofit:2.5.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.5.0")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.github.bumptech.glide:glide:4.13.2")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:3.4.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}