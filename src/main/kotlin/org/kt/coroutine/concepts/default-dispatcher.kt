package org.kt.coroutine.concepts

import kotlinx.coroutines.*
import org.kt.coroutine.utils.log

fun main() {
    runBlocking<Unit> {
        launch(Dispatchers.Default) {
            println("I'm executing in ${Thread.currentThread().name} ")
        }
    }
    testMain()

    Thread.sleep(500)
}

fun testMain() : Job {
    return CoroutineScope(Dispatchers.IO).launch {
        log("Exec the task")
    }
}