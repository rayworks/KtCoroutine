package org.kt.coroutine.concepts

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
        val slow: Deferred<Int> = async {
            var result = 0
            delay(1000)
            for (i in 1..10) {
                result += i
            }
            println("Call complete for slow : $result")
            result
        }

        val quick: Deferred<Int> = async {
            delay(100)
            println("Call complete for quick: 5")
            5
        }

        val result: Int = quick.await() + slow.await()
        println(result)
    }