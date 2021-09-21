package org.kt.coroutine.structed_concurrency

import kotlinx.coroutines.*
import java.io.IOException

fun main() = runBlocking {
    val ceh = CoroutineExceptionHandler { _, exception ->
        println("Caught original $exception")
    }
    val scope = CoroutineScope(coroutineContext /* + ceh */ + Job())

    val job = scope.launch(ceh) {
        launch {
            try {
                delay(Long.MAX_VALUE)
            } finally {
                println("Child 1 is cancelled")
            }
        }
        launch {
            delay(1000)

            throw IOException()
        }
    }
    job.join()
}