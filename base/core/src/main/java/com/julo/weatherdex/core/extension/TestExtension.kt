package com.julo.weatherdex.core.extension

import org.junit.Assert.*
import kotlin.math.abs
import kotlin.random.Random

infix fun Any?.assertEqual(target: Any?) = assertEquals(this, target)

infix fun <T> Collection<T>?.assertListEqualToIgnoreOrder(other: Collection<T>): Boolean {
    if (this !== other) {
        if (this?.size != other.size) return false
        val areNotEqual = this.asSequence()
            .map { it in other }
            .contains(false)
        if (areNotEqual) return false
    }
    return true
}

fun <T> Collection<T>.assertIsEmpty() {
    this.isEmpty().assertBoolean()
}

fun Any?.assertNull() = assertNull(this)

fun Boolean.assertBoolean(flag: Boolean = true) {
    if(flag) assertTrue(this) else assertFalse(this)
}

fun randomLong(absolute: Boolean = false): Long {
    val value = Random.nextLong()
    return if(absolute) abs(value) else value
}

fun randomDouble(absolute: Boolean = false): Double {
    val value = Random.nextDouble()
    return if(absolute) abs(value) else value
}

fun randomInt(absolute: Boolean = false): Int {
    val value = Random.nextInt()
    return if(absolute) abs(value) else value
}

fun randomString(length: Int = 10): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun randomBoolean() = Random.nextBoolean()

fun <T: Enum<*>> randomEnum(enumClass: Class<T>): T {
    val enums: Array<T> = enumClass.enumConstants as Array<T>
    val randomIndex = Random.nextInt(enums.size)
    return enums[randomIndex]
}
