package org.kt.coroutine.concepts

import kotlinx.coroutines.*
import org.kt.coroutine.utils.log

suspend fun test() {

    log("task started")

    coroutineScope {
        launch {
            log("> before task executing")
            delay(400)
            log("> after  task executing")
        }

        launch {
            log("> 'before task executing")
            delay(300)
            log("> 'after  task executing")
        }

    }
    log("task end")

}

suspend fun main() {
    runBlocking {
        log("hello")

        launch {
            log("Hi cor-1")
            delay(100)
            log("Bye cor-1")
        }

        launch {
            log("Hi cor-2")
            delay(200)
            log("Bye cor-2")
        }
        log("Bye")
    }

    log("before test")
    test()
    log("after test")
}