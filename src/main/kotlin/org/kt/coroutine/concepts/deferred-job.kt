package org.kt.coroutine.concepts

import kotlinx.coroutines.*

fun main() = runBlocking {
    val slow: Deferred<Int> = async {
        var result = 0
        delay(3000)
        for (i in 1..10) {
            result += i
        }
        println("Call complete for slow : $result ,thrd:${Thread.currentThread().name}")
        result
    }

    val quick: Deferred<Int> = async {
        delay(2000)
        println("Call complete for quick: 5 ,thrd:${Thread.currentThread().name}")
        5
    }

    println(System.currentTimeMillis().toString() + " Before")
    val result: Int = quick.await() + slow.await()
    println(System.currentTimeMillis())

    println(result)
}