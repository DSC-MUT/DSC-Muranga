plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.servicesPlugin)
}

android {
    compileSdkVersion(AndroidSDK.compileSdk)
    buildToolsVersion("30.0.3")

    defaultConfig {
        applicationId = "tech.danielwaiguru.dscmuranga"
        minSdkVersion(AndroidSDK.minSdk)
        targetSdkVersion(AndroidSDK.targetSdk)
        versionCode = 1
        versionName = "0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
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
}

dependencies {

    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.coreKtx)
    implementation(Libraries.appCompat)
    implementation(Libraries.materialComponents)
    implementation(Libraries.constraintLayout)
    //Ktx
    implementation(Libraries.fragmentKtx)
    implementation(Libraries.livedataKtx)
    implementation(Libraries.livedataKtx)
    implementation(Libraries.navigationKtx)
    implementation(Libraries.fragmentNav)
    implementation(Libraries.uiNav)
    implementation(Libraries.viewmodelKtx)
    implementation(Libraries.lifecycleKtx)
    //Coroutines
    implementation(Libraries.coroutinesCore)
    implementation(Libraries.coroutinesAndroid)
    //Firebase
    //implementation(Libraries.bomPlatform)
    implementation(Libraries.firebaseAnalytics)
    implementation(Libraries.firebaseAuth)
    implementation(Libraries.googleSignIn)
    implementation(Libraries.firestore)
    implementation(Libraries.storage)
    //Coroutines firebase
    implementation(Libraries.coroutinesFirebase)
    //Koin DI
    implementation(Libraries.koinAndroid)
    implementation(Libraries.koinTest)
    implementation(Libraries.koinViewModel)
    //Sweet Alert Dialog
    implementation(Libraries.sweetAlertDialog)
    //Unit Tests
    testImplementation(TestLibraries.junit)
    //Instrumentation Tests
    androidTestImplementation(TestLibraries.testRunner)
    androidTestImplementation(TestLibraries.expresso)

}