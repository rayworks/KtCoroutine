package org.kt.coroutine.internal

import mu.KotlinLogging
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

val logger = KotlinLogging.logger {}

val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor {
    Thread(it, "sched").apply { isDaemon = true }
}

// light-weight implementation of the official [kotlinx.coroutines.Delay#delay]
suspend fun delay(timeMillis: Long) {
    //
    // we suspend a coroutine, not a function
    // “Suspending functions are not coroutines, just functions that can suspend a coroutine”
    //
    suspendCoroutine { cont ->
        // scheduleResumeAfterDelay
        executor.schedule({
            logger.info("resuming now")

            cont.resume(Unit)

        }, timeMillis, TimeUnit.MILLISECONDS)
    }

}

suspend fun main() {
    logger.info("Before")
//    kotlinx.coroutines.delay(1000)
    delay(1000)

    /* suspendCoroutine { continuation ->
         thread {
             println("suspended")
             Thread.sleep(1000)
             continuation.resume(Unit)
             println("Resumed")
         }
 //        println("Before too")
 //        continuation.resume(Unit) // resume
     }*/

    logger.info("After")
}