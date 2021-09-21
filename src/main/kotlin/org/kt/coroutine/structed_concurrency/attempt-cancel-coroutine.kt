package org.kt.coroutine.structed_concurrency

import kotlinx.coroutines.*


val startTime = System.currentTimeMillis()

fun main() = runBlocking {
    withContext(Dispatchers.Unconfined) {
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            while (true) { // child coroutine is still running despite the cancellation from the parent
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
