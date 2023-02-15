package com.julo.weatherdex.base.network.data

sealed interface HttpResponse<out T> {
    object Loading : HttpResponse<Nothing>

    open class Error(
        open val message: String,
        val meta: Map<String, Any?> = mapOf(),
    ) : HttpResponse<Nothing>

    object Empty : HttpResponse<Nothing>

    class Success<T>(
        val data: T,
        val meta: Map<String, Any?> = mapOf(),
    ) : HttpResponse<T>
}
