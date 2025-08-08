package com.martorell.albert.data

import retrofit2.HttpException
import java.io.IOException

/**
 * This custom error class is meant for Flows approach
 */
sealed class CustomErrorFlow {
    class Server(val code: Int) : CustomErrorFlow()
    data object Connectivity : CustomErrorFlow()
    class Unknown(message: String) : CustomErrorFlow()
}

fun Throwable.toCustomErrorFlow(): CustomErrorFlow =

    when (this) {
        is IOException -> CustomErrorFlow.Connectivity
        is HttpException -> CustomErrorFlow.Server(code())
        else -> CustomErrorFlow.Unknown(message ?: "")
    }

/**
 * This is a Utils function that wraps the requests in order to convert it into a CustomError
 * If there is an exception we convert it into a CustomError, else it returns null
 */
inline fun <T> customFlowTryCatch(action: () -> T): CustomErrorFlow? = try {
    action()
    null
} catch (ex: Exception) {
    ex.toCustomErrorFlow()
}