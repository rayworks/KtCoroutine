package org.kt.coroutine.structed_concurrency

import kotlinx.coroutines.*

fun main() = runBlocking {
    withContext(Dispatchers.Unconfined) {
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            while (isActive) { // returns true when the current job is still active (not complete, not cancelled yet)
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job : I'm working...")
                    nextPrintTime += 500
                }
            }
        }
        delay(1200)
        println("main : I'm going to cancel the job")
        job.cancel()
        println("main : Done")
    }
}
