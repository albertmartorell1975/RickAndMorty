package com.martorell.albert.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import retrofit2.HttpException
import java.io.IOException

typealias ResultResponse<T> = Either<CustomError, T>

/**
 * This custom error class is meant for requests approach, which can be two sides of response (right or left) that follow the Either pattern:
 * -> Left is the wrong response
 * -> Right is the expected response
 */
sealed class CustomError {

    class Server(val code: Int) : CustomError()
    object Connectivity : CustomError()
    class Unknown(val message: String) : CustomError()
}

fun Exception.toCustomError(): CustomError =

    when (this) {
        is IOException -> CustomError.Connectivity
        is HttpException -> CustomError.Server(code())
        else -> CustomError.Unknown(message ?: "")
    }

inline fun <T> customTryCatch(action: () -> T): ResultResponse<T> = try {
    action().right()
} catch (ex: IllegalArgumentException) {
    ex.toCustomError().left()
} catch (ex: Exception) {
    ex.toCustomError().left()
}