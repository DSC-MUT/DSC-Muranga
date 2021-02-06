const val kotlinVersion = "1.4.21"

object BuildPlugins {
    object Versions {
        const val buildToolsVersion = "4.1.2"
        const val googleServicesVersion = "4.3.5"
    }
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "kotlin-android"
    const val servicesPlugin = "com.google.gms.google-services"
    const val googleServices = "com.google.gms:google-services:${Versions.googleServicesVersion}"
}
object AndroidSDK {
    const val minSdk = 21
    const val targetSdk = 30
    const val compileSdk = targetSdk
}
object Libraries {
    private object Versions {
        const val jetpackVersion = "1.2.0"
        const val coreKtxVersion = "1.3.2"
        const val materialComponentsVersion = "1.2.1"
        const val constraintVersion = "2.0.4"
        const val fragmentKtxVersion = "1.2.5"
        const val lifcycleKtxVersion = "2.2.0"
        const val navigationKtxVersion = "2.3.3"
        const val coroutinesVersion = "1.4.2"
        const val bomVersion = "26.4.0"
        const val playServicesVersion = "19.0.0"
        const val coroutinesFirebaseVersion = "1.1.1"
    }
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtxVersion}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.jetpackVersion}"
    const val materialComponents = "com.google.android.material:material:${Versions.materialComponentsVersion}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintVersion}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtxVersion}"
    const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifcycleKtxVersion}"
    const val livedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifcycleKtxVersion}"
    const val navigationKtx = "androidx.navigation:navigation-runtime-ktx:${Versions.navigationKtxVersion}"
    const val fragmentNav = "androidx.navigation:navigation-fragment-ktx:${Versions.navigationKtxVersion}"
    const val uiNav = "androidx.navigation:navigation-ui-ktx:${Versions.navigationKtxVersion}"
    const val viewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifcycleKtxVersion}"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVersion}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesVersion}"
    const val bomPlatform = "com.google.firebase:firebase-bom:${Versions.bomVersion}"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
    const val firebaseAuth = "com.google.firebase:firebase-auth-ktx"
    const val googleSignIn = "com.google.android.gms:play-services-auth:${Versions.playServicesVersion}"
    const val firestore = "com.google.firebase:firebase-firestore-ktx"
    const val storage = "com.google.firebase:firebase-storage-ktx"
    const val coroutinesFirebase = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutinesFirebaseVersion}"
}
object TestLibraries {
    private object Versions {
        const val junitVersion = "4.13.1"
        const val expressoVersion = "3.3.0"
        const val testRunnerVersion = "1.1.2"
    }
    const val junit = "junit:junit:${Versions.junitVersion}"
    const val testRunner = "androidx.test.ext:junit:${Versions.testRunnerVersion}"
    const val expresso = "androidx.test.espresso:espresso-core:${Versions.expressoVersion}"
}
object BuildModules {
    const val App = ":app"
}