package org.kt.coroutine.structed_concurrency

import kotlinx.coroutines.*

private suspend fun wasteCpu2() = withContext(Dispatchers.Default) {
    var nextPrintTime = System.currentTimeMillis()
    while (true) {
        delay(10) // suspendCancellableCoroutine inside
        if (System.currentTimeMillis() >= nextPrintTime) {
            println("job: I'm working...")
            nextPrintTime += 500
        }
    }
}

fun main() = runBlocking {
    val job = launch {
        try {
            // you should make your suspending functions cancellable
            wasteCpu2()
        } catch (e: CancellationException) {
            // handle cancellation
            e.printStackTrace()
        }
    }
    delay(200)
    job.cancelAndJoin()
    println("Done")
}