package org.kt.coroutine.flows

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlin.RuntimeException

val upstream = flowOf(1, 3, -1)

val encapsulateError = upstream
    .onEach {
        if (it < 0) throw NumberFormatException("Value  should be greater than 0")
    }
    .catch { e ->
        println("Caught $e")
    }

fun main() = runBlocking {
/*    try {
        upstream.collect { value ->
//            if (value > 2)
//                throw RuntimeException()
            println("Received $value")
        }
    } catch (e: Throwable) {
        println("Caught $e")
    }*/

    try {
        encapsulateError.collect {
            if (it > 2) throw RuntimeException()
            println("Received $it")
        }
    } catch (e: RuntimeException) { // catch operator acts as a safeguard (logs the exception and cancel the flow)
        println("Collector stopped collecting the flow")
    }
//    encapsulateError.collect {
//        println("Received $it")
//    }
}