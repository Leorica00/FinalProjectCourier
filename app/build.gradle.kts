import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    packaging {
        resources.excludes.add("META-INF/*")
    }

    namespace = "com.example.finalprojectcourier"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.finalprojectcourier"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        debug {
            buildConfigField("String", "FIREBASE_API_SERVICE", "\"https://finalproject-4ba60-default-rtdb.europe-west1.firebasedatabase.app/\"")
            buildConfigField("String", "GOOGLE_BASE_URL", "\"https://maps.googleapis.com/\"")
            buildConfigField("String", "MAP_API_KEY", gradleLocalProperties(rootDir).getProperty("MAP_API_KEY"))
            buildConfigField("String", "MOCKY_BASE_URL", "\"https://run.mocky.io/v3/\"")
            buildConfigField("String", "CHATBOT_BASE_URL", "\"https://dialogflow.googleapis.com\"")
        }

        release {
            buildConfigField("String", "FIREBASE_API_SERVICE", "\"https://finalproject-4ba60-default-rtdb.europe-west1.firebasedatabase.app/\"")
            buildConfigField("String", "GOOGLE_BASE_URL", "\"https://maps.googleapis.com/\"")
            buildConfigField("String", "MAP_API_KEY", gradleLocalProperties(rootDir).getProperty("MAP_API_KEY"))
            buildConfigField("String", "MOCKY_BASE_URL", "\"https://run.mocky.io/v3/\"")
            buildConfigField("String", "CHATBOT_BASE_URL", "\"https://dialogflow.googleapis.com\"")
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
        buildConfig = true
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation("com.google.auth:google-auth-library-oauth2-http:1.23.0")

    implementation("com.google.android.libraries.places:places:3.3.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("com.google.maps.android:android-maps-utils:3.8.0")

    implementation("com.hbb20:ccp:2.5.0")

    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-database-ktx:20.3.1")
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.3")

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("com.google.firebase:firebase-sessions:1.2.2")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-android-compiler:2.50")

    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}