package org.kt.coroutine.structed_concurrency

import kotlinx.coroutines.*
import java.io.IOException

fun main() = runBlocking {
    val scope = CoroutineScope(coroutineContext + Job())

    val job = scope.launch {
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