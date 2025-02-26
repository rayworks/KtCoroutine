package org.kt.coroutine.structed_concurrency

import kotlinx.coroutines.*
import java.io.IOException

fun main() = runBlocking {
    // It doesn't matter whether we will use SupervisorJob or not,
    // the exception will be propagating further and crash the main thread eventually.
    val scope = CoroutineScope (coroutineContext + Job())

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