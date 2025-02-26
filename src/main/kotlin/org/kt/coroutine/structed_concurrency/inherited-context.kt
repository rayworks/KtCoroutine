package org.kt.coroutine.structed_concurrency

import kotlinx.coroutines.*
import org.kt.coroutine.utils.log

@OptIn(ExperimentalCoroutinesApi::class)
fun main() {
    runBlocking(Dispatchers.Default) {
        log(coroutineContext.toString())

        launch {
            log(coroutineContext.toString())
            log("current job : ${coroutineContext.job}")

            launch(Dispatchers.IO + CoroutineName("mine")) {
                log(coroutineContext.toString())
                log("mine job's parent ${coroutineContext.job.parent}")

                launch {
                    log(coroutineContext.toString())
                }
            }
        }
    }
}