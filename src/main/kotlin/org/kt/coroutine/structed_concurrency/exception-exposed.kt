package org.kt.coroutine.structed_concurrency

import kotlinx.coroutines.*

fun main() = runBlocking {
    val scope = CoroutineScope(Job())

    val job = scope.launch {
        // If 'coroutineScope' used here,
        // program will crash, async exposes uncaught exceptions but also exposes to its parent the exception
        // that caused its failure, then scope.launch treats this exception as unhandled.
        supervisorScope {
            val task1 = launch {
                delay(100)
                println("Done a background task")
            }

            val task2 = async {
                delay(500)

                throw Exception()
            }

//            task2.await()
            try {
                task2.await()
            } catch (e: Exception) {
                coroutineContext.ensureActive()
                // process the error
                println("Caught exception $e")
            }
            task1.join()

            delay(100)
            task2.cancel()
        }
    }

    delay(300)
    job.cancelAndJoin()
//    job.join()
    println("Program ends")
}