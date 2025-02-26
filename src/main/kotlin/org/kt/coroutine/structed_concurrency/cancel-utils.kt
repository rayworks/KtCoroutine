package org.kt.coroutine.structed_concurrency

import kotlinx.coroutines.*
import java.lang.Thread.sleep
import kotlin.coroutines.coroutineContext


suspend fun log(str: String) {
    println("[${Thread.currentThread().name}@${coroutineContext}] $str")
}

suspend fun doCpuHeavyWork(): Int {
    log("I'm working...")

    var counter = 0
    val startTime = System.currentTimeMillis()
    while (System.currentTimeMillis() - startTime < 500) {
        counter++

        // allow dispatcher to switch to working on different coroutines if possible.
        yield()
    }
    return counter
}

fun main() {
    runBlocking(Dispatchers.IO) {
        val job = async {
            repeat(3) {
                //ensureActive()
                doCpuHeavyWork()

                // check point for cancellation :
                // ensureActive()
                // OR
                // if (!isActive) return@launch
            }
        }

        launch {
            repeat(3) {
                doCpuHeavyWork()
                //if (!isActive) return@launch
            }
        }

        sleep(100)
        println("cancelling first job...")

        job.cancelAndJoin()
    }
}