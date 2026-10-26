import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.koin.android)
            implementation(libs.androidx.activity.compose)
            implementation(libs.accompanist.permissions)
            implementation("com.airbnb.android:lottie-compose:6.3.0") // for Compose
            implementation("com.airbnb.android:lottie:6.3.0") // for LottieAnimationView
            implementation(libs.maps.compose)//  when using Jetpack Compose
            implementation(libs.play.services.maps)//to show Google Maps

            // ✅ firebase
            implementation(libs.firebase.auth)
            implementation(libs.firebase.analytics)
            implementation(libs.firebase.crashlytics)
            implementation(libs.firebase.messaging)
            implementation ("com.github.bumptech.glide:glide:4.16.0")

            implementation(libs.gson)
// Stripe
            implementation(libs.stripe.android)


            // Ktor Android
            implementation(libs.ktor.client.okhttp)

            implementation("io.socket:socket.io-client:2.1.0") {
                exclude(group = "org.json", module = "json") // avoid json conflicts
            }

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
//            implementation(libs.androidx.lifecycle.viewmodel)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtime.compose)

            // CORE kotlinx.serialization library for general JSON tasks
            implementation(libs.kotlinx.serialization.json) // Or latest stable version

            // ✅ Ktor for networking
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)

            // ✅ Dependency Injection (Koin)
            implementation(project.dependencies.platform(libs.koin.bom))
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.navigation.compose)


//            // ✅ coroutines
//            implementation(libs.kotlinx.coroutines.core)

            // ✅ WebView
            api(libs.compose.webview.multiplatform)

            // ✅ Shared Preferences
            implementation(libs.multiplatform.settings.no.arg)

            // GeoLocation
            implementation(libs.compass.geolocation)
            implementation(libs.compass.geolocation.mobile)
            // ✅ Wheel Type Date Picker
            implementation(libs.kotlinx.datetime)
            implementation(libs.kmp.date.time.picker)

            // ✅ Image Loading COIL dependency
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            // Geocoding
            implementation(libs.compass.geocoder)
            implementation(libs.compass.geocoder.mobile)

            // Location permissions for mobile
            implementation(libs.compass.permissions.mobile)

            implementation("com.soywiz.korlibs.krypto:krypto:4.0.0")

            //DateFormat
            implementation(libs.kotlinx.datetime.v062)

            implementation (libs.androidx.compose.bom)


        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.bytheour"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.bytheour"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 26031701
        versionName = "1.3.3"
    }

    signingConfigs {
        create("release") {
            storeFile = file("../composeApp/src/androidMain/bytheour")
            storePassword = "bytheour123"
            keyAlias = "key0"
            keyPassword = "bytheour123"
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

