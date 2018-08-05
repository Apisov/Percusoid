@file:Suppress("unused")

object Versions {
    const val minSdk = 18
    const val compileSdk = 27
    const val buildTools = "27.0.3"

    const val kotlin = "1.2.60"
    const val support = "27.1.1"
    const val constraintLayout = "1.1.2"
    const val rxJava = "2.1.10"
    const val rxAndroid = "2.0.2"
    const val rxKotlin = "2.2.0"
    const val room = "1.0.0"
    const val archLifecycle = "1.1.1"
    const val anko = "0.10.1"
    const val koin = "0.9.1"
    const val dataBinding = "3.1.1"

    const val androidGradlePlugin = "3.1.3"
    const val gradleVersionsPlugin = "0.17.0"
}

object Deps {
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val koin = "org.koin:koin-android-architecture:${Versions.koin}"
    const val anko = "org.jetbrains.anko:anko-common:${Versions.anko}"

    const val appcompat = "com.android.support:appcompat-v7:${Versions.support}"
    const val support_v4 = "com.android.support:support-v4:${Versions.support}"
    const val design = "com.android.support:design:${Versions.support}"
    const val recyclerView = "com.android.support:recyclerview-v7:${Versions.support}"
    const val archLifecycle = "android.arch.lifecycle:extensions:${Versions.archLifecycle}"
    const val archLifecycleCompiler = "android.arch.lifecycle:compiler:${Versions.archLifecycle}"
    const val constraintLayout =
        "com.android.support.constraint:constraint-layout:${Versions.constraintLayout}"

    const val room = "android.arch.persistence.room:runtime:${Versions.room}"
    const val roomRxJava = "android.arch.persistence.room:rxjava2:${Versions.room}"
    const val roomCompiler = "android.arch.persistence.room:compiler:${Versions.room}"

    const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"

    const val androidGradlePlugin =
        "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"

    const val gradleVersionsPlugin =
        "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleVersionsPlugin}"

    const val dexcountGradlePlugin =
        "com.getkeepsafe.dexcount:dexcount-gradle-plugin:0.8.2"
}