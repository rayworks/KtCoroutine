package org.kt.coroutine.structed_concurrency

import kotlinx.coroutines.*
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun Call.await() =
    suspendCancellableCoroutine<ResponseBody?> { continuation ->
        continuation.invokeOnCancellation {
            cancel()
        }

        enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                continuation.resume(response.body, onCancellation = { e ->
                    e.printStackTrace()
                })
            }

        })
    }


val okHttpClient = OkHttpClient()
val request = Request.Builder().url("https://publicobject.com/helloworld.txt")
    .build()

suspend fun performHttpRequest(): ResponseBody? = withContext(Dispatchers.IO) {
    val call = okHttpClient.newCall(request)
    call.await()
}

fun main() = runBlocking {
    val job = launch {
        val resp = performHttpRequest()
        println("Got response ${resp?.string()}")
    }
    delay(200)
//    job.cancelAndJoin()
    job.join()
    println("Done")
}