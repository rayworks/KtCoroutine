package org.kt.coroutine.concepts

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val job = launch() {
        var i = 0;
        while (true) {
            println("$i I'm still working...")
            i++
//            Thread.sleep(10) // blocking
            delay(10) // non-blocking
        }
    }
    delay(30)
    job.cancel()
}