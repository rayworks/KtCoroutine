package org.kt.coroutine.structed_concurrency

import kotlinx.coroutines.*
import java.io.IOException

fun main() = runBlocking {
    val ceh = CoroutineExceptionHandler { _, exception ->
        println("Caught original $exception")
    }

    // Exception handling:
    // When :The exception is thrown by a coroutine that automatically throws exceptions (works with launch, not with async).
    // Where : If it’s in the CoroutineContext of a CoroutineScope or a root coroutine (direct child of CoroutineScope or a supervisorScope)
    val range = CoroutineScope(Job())
    val task = range.launch(ceh) {
        // The inner launch will propagate the exception up to the parent as soon as it happens, since the parent
        // doesn't know anything about the handler, the exception will be thrown.
        launch() {
            throw Exception("Inner failed")
        }
    }
    task.join()

    val scope = CoroutineScope(coroutineContext  + /*ceh  +*/ SupervisorJob())
    val job = scope.launch(ceh) {
        try {
            //
            // When async is used as a root coroutine, exceptions are thrown when you call await
            //
            val deferred = async {
                throw IllegalStateException("Test illegal async state ")
            }
            deferred.await()
        } catch (e: Exception) {
            // Exception thrown in async WILL NOT be caught here
            // but propagated up to the scope

            // The exception is thrown by a coroutine that automatically throws exceptions (works with launch, not with async).
            // https://medium.com/androiddevelopers/exceptions-in-coroutines-ce8da1ec060c
            println(">>> async exception")
            e.printStackTrace()
        }
    }
    job.join()
}