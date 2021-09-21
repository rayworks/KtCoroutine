package org.kt.coroutine.structed_concurrency

import kotlinx.coroutines.*

suspend fun wasteCpu() = withContext(Dispatchers.Default) {
    var nextPrintTime = System.currentTimeMillis()
    while (isActive) {
        if (System.currentTimeMillis() >= nextPrintTime) {
            println("job: I'm working...")
            nextPrintTime += 500
        }
    }
    if (!isActive) {
        println("Cleanup started...")
    }
}

fun main() = runBlocking {
    var job = launch {
        try {
            wasteCpu()
        } catch (e: CancellationException) {
            // handle cancellation
            e.printStackTrace()
        }
    }
    delay(200)
    job.cancelAndJoin()
    println("Done")
}