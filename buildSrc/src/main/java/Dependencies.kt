import org.gradle.api.artifacts.dsl.DependencyHandler

object Versions {
    const val minSdk = 21
    const val compileSdk = 28
    const val buildTools = "28.0.2"

    const val kotlin = "1.3.61"
    const val support = "27.1.1"
    const val constraintLayout = "1.1.3"
    const val rxJava = "2.1.10"
    const val rxAndroid = "2.0.2"
    const val rxKotlin = "2.2.0"
    const val room = "1.1.1"
    const val archLifecycle = "1.1.1"
    const val anko = "0.10.1"
    const val koin = "2.0.1"

    const val androidGradlePlugin = "4.0.0-alpha08"
    const val gradleVersionsPlugin = "0.17.0"

    /* TESTING */
    const val junit = "4.12"
    const val mockito = "2.7.19"
    const val testRunner = "1.0.2"
    const val espressoCore = "3.0.2"
    const val hamcrest = "1.3"
}

object Deps {
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlinTest = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    const val kotlinAllOpenPlugin = "org.jetbrains.kotlin:kotlin-allopen:${Versions.kotlin}"

    val koin = arrayOf(
            "org.koin:koin-android:${Versions.koin}",
            "org.koin:koin-android-scope:${Versions.koin}",
            "org.koin:koin-android-viewmodel:${Versions.koin}"
    )

    const val anko = "org.jetbrains.anko:anko-common:${Versions.anko}"

    const val appcompat = "com.android.support:appcompat-v7:${Versions.support}"
    const val support_v4 = "com.android.support:support-v4:${Versions.support}"
    const val design = "com.android.support:design:${Versions.support}"
    const val recyclerView = "com.android.support:recyclerview-v7:${Versions.support}"
    const val archLifecycle = "android.arch.lifecycle:extensions:${Versions.archLifecycle}"
    const val commonLifecycle = "android.arch.lifecycle:common-java8:${Versions.archLifecycle}"
    const val archTesting = "android.arch.core:core-testing:${Versions.archLifecycle}"
    const val constraintLayout =
            "com.android.support.constraint:constraint-layout:${Versions.constraintLayout}"

    const val room = "android.arch.persistence.room:runtime:${Versions.room}"
    const val roomRxJava = "android.arch.persistence.room:rxjava2:${Versions.room}"
    const val roomCompiler = "android.arch.persistence.room:compiler:${Versions.room}"
    const val roomTesting = "android.arch.persistence.room:testing:${Versions.room}"

    const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"

    const val androidGradlePlugin =
            "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"

    const val gradleVersionsPlugin =
            "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleVersionsPlugin}"

    const val dexcountGradlePlugin =
            "com.getkeepsafe.dexcount:dexcount-gradle-plugin:0.8.2"

    /* TESTING */
    const val junit = "junit:junit:${Versions.junit}"
    const val mockitoCore = "org.mockito:mockito-core:${Versions.mockito}"
    const val hamcrest = "org.hamcrest:hamcrest-all:${Versions.hamcrest}"
    const val testRunner = "com.android.support.test:runner:${Versions.testRunner}"
    const val espressoCore =
            "com.android.support.test.espresso:espresso-core:${Versions.espressoCore}"
}

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
fun DependencyHandler.`implementations`(dependencyNotation: Array<String>) =
        dependencyNotation.forEach { add("implementation", it) }
