plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.hs_test_bc"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.hs_test_bc"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

    }


    signingConfigs {
        create("release") {
            storeFile = file("keystore.keystore")
            storePassword = project.findProperty("RELEASE_STORE_PASSWORD") as String? ?: "CwjdurGhdMitL29q4xovbepk"
            keyAlias = project.findProperty("RELEASE_KEY_ALIAS") as String? ?: "upload"
            keyPassword = project.findProperty("RELEASE_KEY_PASSWORD") as String? ?: "wxGAG6dpmGsveZiYPG4xEzsD"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField ("String", "API_URL", "\"https://api.github.com/\"")
            buildConfigField ("String", "CLIENT_ID", "\"Ov23lic9Zdt9If7rLC5G\"")
            buildConfigField ("String", "CLIENT_SECRET", "\"f041053e5d4f703ccbf1ec740501a8e0c1ec33e1\"")

            val redirectHost = "oauth"
            val redirectScheme = "hsbc"
            buildConfigField ("String", "REDIRECT_HOST", "\"${redirectHost}\"")
            buildConfigField ("String", "REDIRECT_SCHEME", "\"${redirectScheme}\"")
            manifestPlaceholders += mapOf("redirectHost" to redirectHost, "redirectScheme" to redirectScheme)
        }

        debug {
            isMinifyEnabled = false
            isShrinkResources = false

            buildConfigField ("String", "API_URL", "\"https://api.github.com/\"")
            buildConfigField ("String", "CLIENT_ID", "\"Ov23lic9Zdt9If7rLC5G\"")
            buildConfigField ("String", "CLIENT_SECRET", "\"f041053e5d4f703ccbf1ec740501a8e0c1ec33e1\"")

            val redirectHost = "oauth"
            val redirectScheme = "hsbc"
            buildConfigField ("String", "REDIRECT_HOST", "\"${redirectHost}\"")
            buildConfigField ("String", "REDIRECT_SCHEME", "\"${redirectScheme}\"")
            manifestPlaceholders += mapOf("redirectHost" to redirectHost, "redirectScheme" to redirectScheme)
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions.unitTests {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.hilt.android.core)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.hilt.android.testing)
    implementation(libs.androidx.annotation)
    ksp(libs.hilt.compiler)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.scalars)
    implementation(libs.retrofit.gson)
    implementation(libs.coil.compose)
    implementation(libs.androidx.datastore)



    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.androidx.espresso.core)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.mockito.core)
    androidTestImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.mockito.inline)
    androidTestImplementation(libs.androidx.espresso.core)

    // To use the androidx.test.core APIs
    androidTestImplementation(libs.androidx.core.v)
    // Kotlin extensions for androidx.test.core
    androidTestImplementation(libs.core.ktx)

    // Kotlin extensions for androidx.test.ext.junit
    androidTestImplementation(libs.androidx.junit.ktx)

    // To use the Truth Extension APIs
    androidTestImplementation(libs.androidx.truth)

    // To use the androidx.test.runner APIs
    androidTestImplementation(libs.androidx.runner)

    // To use android test orchestrator
    androidTestUtil(libs.androidx.orchestrator)
    testImplementation(kotlin("test"))


}