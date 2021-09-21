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
                throw Exception()
            }

//            task2.await()
            try {
                task2.await()
            } catch (e: Exception) {
                println("Caught exception $e")
            }
            task1.join()
        }
    }

    job.join()
    println("Program ends")
}