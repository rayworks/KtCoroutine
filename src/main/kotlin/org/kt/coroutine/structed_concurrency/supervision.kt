package org.kt.coroutine.structed_concurrency

import kotlinx.coroutines.*
import java.lang.AssertionError

fun main() = runBlocking {
    val ceh = CoroutineExceptionHandler { _, e ->
        println("Handled $e")
    }

    // it only propagates cancellation downwards, and cancels all children only if it has failed itself
    val supervisor = SupervisorJob()

    val scope = CoroutineScope(coroutineContext /* + ceh */ + supervisor)
    with(scope) {
        val firstChild = launch(ceh) {
            println("First child is failing")
            throw AssertionError("First child is cancelled")
        }

        val secondChild = launch {
            firstChild.join()

            delay(10) // playing nice with hypothetical cancellation
            println("First child is cancelled: ${firstChild.isCancelled}, but second one is still alive")
        }

        // wait until the second child completes
        secondChild.join()
    }

}