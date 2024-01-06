package org.kt.coroutine.flows

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

fun members(): Flow<Int> = flow {
    emit(1)
    emit(2)
}

@InternalCoroutinesApi
fun main() = runBlocking {
    testSequence()

    val flow = members()
    flow.collect { value -> println("$value") }
}

private fun testSequence() {
    println(">>> start generating fibonacci numbers")
    generateFib().take(5).forEach { println(it) }

    generateSequence(2) { it * 2 }.take(10).forEach { println(it) }
}


private fun generateFib() = sequence {
    var pre = 0
    var curr = 1
    while (true) {
        yield(curr)

        val last = curr
        curr += pre
        pre = last
    }
}