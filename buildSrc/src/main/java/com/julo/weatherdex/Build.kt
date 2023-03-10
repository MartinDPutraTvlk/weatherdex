package com.julo.weatherdex

import com.julo.weatherdex.libs.Google

object Build {
    private const val gradleVersion = "4.2.2"
    const val gradle = "com.android.tools.build:gradle:$gradleVersion"

    private const val gradlePluginVersion = "1.6.10"
    const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$gradlePluginVersion"

    const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Google.hiltVersion}"
    const val kotlinSerialization = "org.jetbrains.kotlin:kotlin-serialization:1.8.0"

    private const val googleServicesVersion = "4.3.10"
    const val googleServices = "com.google.gms:google-services:$googleServicesVersion"

    const val jUnitRunner = "androidx.test.runner.AndroidJUnitRunner"

    private const val jetbrainsVersion = "1.6.10"
    const val jetbrains = "org.jetbrains.kotlin:kotlin-gradle-plugin:$jetbrainsVersion"
}
