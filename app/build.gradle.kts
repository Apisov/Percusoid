plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-android-extensions")
    id("com.github.ben-manes.versions")
    id("kotlin-allopen")
}

android {
    compileSdkVersion(Versions.compileSdk)
    buildToolsVersion(Versions.buildTools)

    defaultConfig {
        applicationId = "com.apisov.percusoid"
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.compileSdk)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.schemaLocation", "$projectDir/schemas")
            }
        }

        sourceSets {
            getByName("androidTest").java.srcDirs("$projectDir/schemas")
        }

        vectorDrawables.useSupportLibrary = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isUseProguard = true
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"))
        }
    }
    dataBinding.isEnabled = true

    flavorDimensions("default")

    compileOptions {
        setSourceCompatibility(JavaVersion.VERSION_1_8)
        setTargetCompatibility(JavaVersion.VERSION_1_8)
    }
}

allOpen {
    // allows mocking for classes w/o directly opening them for release builds
    annotation("com.apisov.percusoid.OpenClass")
}

dependencies {
    /*ANDROID*/
    implementation(Deps.constraintLayout)
    implementation(Deps.appcompat)
    implementation(Deps.design)
    implementation(Deps.room)
    implementation(Deps.roomRxJava)
    implementation(Deps.archLifecycle)
    implementation("android.arch.lifecycle:livedata:${Versions.archLifecycle}")
    implementation(Deps.commonLifecycle)

    implementation("com.shawnlin:number-picker:2.4.6")

    kapt(Deps.roomCompiler)

    /*KOTLIN*/
    implementation(Deps.anko)
    implementation(Deps.kotlinStdlib)
    implementations(Deps.koin)

    // RX
    implementation(Deps.rxJava)
    implementation(Deps.rxKotlin) {
        exclude(group = "rxjava")
    }
    implementation(Deps.rxAndroid) {
        exclude(group = "rxjava")
    }

    /*OTHER*/
    implementation("com.illposed.osc:javaosc-core:0.4")
    implementation("com.jakewharton.timber:timber:4.7.1")

    /* TESTING */
    testImplementation(Deps.junit)
    testImplementation(Deps.mockitoCore)
    testImplementation(Deps.hamcrest)
    testImplementation(Deps.kotlinStdlib)
    testImplementation(Deps.kotlinTest)

    testImplementation(Deps.archTesting) {
        exclude(group = "com.android.support", module = "support-compat")
        exclude(group = "com.android.support", module = "support-annotations")
        exclude(group = "com.android.support", module = "support-core-utils")
    }

    androidTestImplementation(Deps.junit)
    androidTestImplementation(Deps.mockitoCore) {
        exclude(group = "net.bytebuddy")
    }

    androidTestImplementation(Deps.testRunner)
    androidTestImplementation(Deps.espressoCore) {
        exclude(group = "com.android.support", module = "support-annotations")
        exclude(group = "com.google.code.findbugs", module = "jsr305")
    }
    androidTestImplementation(Deps.archTesting)
}