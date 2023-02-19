package com.julo.weatherdex.core.extension

infix fun String?.equalsIgnoreCase(other: String?) = this.equals(other, true)

infix fun String?.notEqualsIgnoreCase(other: String?) = !(this equalsIgnoreCase other)

infix fun String?.containsIgnoreCase(other: CharSequence) = this?.contains(other, true) ?: false

infix fun String?.containedInEither(others: Collection<String>) = others.any { this containsIgnoreCase it }

infix fun String?.isEither(others: Collection<String>) = others.any { it equalsIgnoreCase this }

infix fun String?.isNeither(others: Collection<String>) = others.none { it equalsIgnoreCase this }

infix fun Collection<String>.isEither(other: String?) = other isEither this

infix fun Collection<String>.isNeither(other: String?) = other isNeither this

fun String?.isNotNullOrEmpty() = !this.isNullOrEmpty()

fun Any?.toStringOrEmpty() = this?.toString().orEmpty()
