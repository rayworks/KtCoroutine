package org.kt.coroutine.structed_concurrency

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.jetbrains.annotations.VisibleForTesting

@get:VisibleForTesting()
val customScope by lazy {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught exception: ${exception.message}")

        if (exception.suppressedExceptions.isNotEmpty()) {
            println("<<< suppressedExceptions dumped >>>")
            for (suppressed in exception.suppressedExceptions) {
                println(">>> suppressed: $suppressed")
            }
        }
    }

    CoroutineScope(SupervisorJob() + Dispatchers.Default)
}