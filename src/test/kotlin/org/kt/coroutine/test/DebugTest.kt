package org.kt.coroutine.test

import kotlinx.coroutines.*
import kotlinx.coroutines.debug.DebugProbes
import org.junit.Test

class DebugTest {
    private suspend fun computeValue(): String = coroutineScope {
        val one = async { computeOne() }
        val two = async { computeTwo() }
        combine(one, two)
    }

    private suspend fun combine(one: Deferred<String>, two: Deferred<String>): String =
        one.await() + two.await()

    private suspend fun computeOne(): String {
        delay(5000)
        return "1"
    }

    private suspend fun computeTwo(): String {
        delay(5000)
        return "2"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun dump(): Unit = runBlocking {
        DebugProbes.install()
        val deferred = async { computeValue() }

        delay(1000)

        // Dump running coroutines
        DebugProbes.dumpCoroutines()

        println("\nDumping only deferred")
        DebugProbes.printJob(deferred)
    }
}
