package com.julo.weatherdex.core.extension

import java.math.BigInteger

fun String?.notNull(defaultNull: String = ""): String = this ?: defaultNull

fun Boolean?.notNull(defaultNull: Boolean = false): Boolean = this ?: defaultNull

fun Float?.notNull(defaultNull: Float = 0f): Float = this ?: defaultNull

fun Double?.notNull(defaultNull: Double = 0.0): Double = this ?: defaultNull

fun Long?.notNull(defaultNull: Long = 0): Long = this ?: defaultNull

fun BigInteger?.notNull(defaultNull: BigInteger = BigInteger.ZERO): BigInteger = this ?: defaultNull

fun Int?.notNull(defaultNull: Int = 0): Int = this ?: defaultNull

fun <T> List<T>?.notNull(defaultNull: List<T> = emptyList()) = this ?: defaultNull

fun <K, V> Map<K, V>?.notNull(defaultNull: Map<K, V> = emptyMap()) = this ?: defaultNull
