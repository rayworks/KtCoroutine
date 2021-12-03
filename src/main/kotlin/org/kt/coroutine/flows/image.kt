package org.kt.coroutine.flows

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import java.io.IOException

data class Image(val url: String)

sealed class Result {
    data class Success(val image: Image) : Result()
    data class Error(val url: String) : Result()
}

suspend fun fetchImage(url: String): Image {
    delay(10)

    if (url.contains("retry")) {
        throw IOException("Server returned HTTP response code 503")
    }

    return Image(url)
}

// Materialize your exceptions
suspend fun fetchResult(url: String): Result {
    println("Fetching $url...")

    return try {
        val image = fetchImage(url)
        Result.Success(image)
    } catch (e: IOException) {
        Result.Error(url)
    }
}

/***
 * A custom [Flow] operator that retries an action while the predicate returns true.
 */
fun <T, R : Any> Flow<T>.mapWithRetry(
    action: suspend (T) -> R,
    predicate: suspend (R, attempt: Int) -> Boolean
) = map { data ->
    var attempt = 0
    var shallRetry: Boolean
    var lastValue: R? = null

    do {
        val tr = action(data)
        shallRetry = predicate(tr, ++attempt)
        if (!shallRetry) lastValue = tr
    } while (shallRetry)

    return@map lastValue
}

fun main() = runBlocking {
    val flow = flowOf(
        "url-1",
        "url-2",
        "url-retry"
    )
    //val resultFlow = flow.map { url -> fetchResult(url) }
    val resultFlowWithRetry = flow.mapWithRetry(::fetchResult) { result, attempt ->
        result is Result.Error && attempt < 3
    }
    val results = resultFlowWithRetry.toList()
    println("Results: $results")
}

