package com.martorell.albert.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import retrofit2.HttpException
import java.io.IOException

// The typealias allow us the avoid type   Either<CustomError, T> each time we use it
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

//  Exception implementation
fun Exception.toCustomError(): CustomError =

    when (this) {
        is IOException -> CustomError.Connectivity
        is HttpException -> CustomError.Server(code())
        else -> CustomError.Unknown(message ?: "")
    }

/*
We abstract the catch body because it will be always the same one.
Given an action that returns a type T, returns ResultResponse<T>
The inline reserverd words not only is to avoid issues with the suspend functions and performance,
but also when the compiler builds this code, instead of call the function into the bytecode,
it will substitute the function call by its itself code.
 */
inline fun <T> customTryCatch(action: () -> T): ResultResponse<T> = try {
    action().right()
} catch (ex: IllegalArgumentException) {
    ex.toCustomError().left()
} catch (ex: Exception) {
    ex.toCustomError().left()
}