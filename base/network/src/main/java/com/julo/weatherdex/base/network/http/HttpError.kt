package com.julo.weatherdex.base.network.http

import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/** Error type when no internet connection. */
object NoInternetError : HttpResponse.Error(message = "No Internet")

/** Error type when connection is time out. */
object TimeOutError : HttpResponse.Error(message = "Time Out")

/** Error type when API response is not 2xx code. */
data class HttpError(
    override val message: String,
    val messageTitle: String,
    val code: Int,
    val data: Any?,
) : HttpResponse.Error(message = message)

/** [data class/object] WhateverError : MoladinResponse.Error(message = ..., meta = ...) */
fun Exception.toError(): HttpResponse.Error {
    return try {
        when {
            this is IOException && message == "No Internet" -> NoInternetError
            this is SocketTimeoutException -> TimeOutError
            this is UnknownHostException -> NoInternetError
            /**
             * Feel free to add other general error type here
             * ...
             */
            this is HttpException -> {
                val error = Gson().fromJson(
                    response()?.errorBody()?.string().orEmpty(),
                    RawHttpError::class.java,
                )
                HttpError(
                    message = error.message ?: message(),
                    messageTitle = error.messageTitle.orEmpty(),
                    code = error.code ?: code(),
                    data = error.data,
                )
            }
            else -> HttpResponse.Error(message = message.orEmpty())
        }
    } catch (e: Exception) {
        HttpResponse.Error(message = e.message.orEmpty())
    }
}
