package org.kt.coroutine.flows

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

suspend fun transform(i: Int): String = withContext(Dispatchers.Default) {
    delay(10)
    "${i + 1}"
}

fun main() = runBlocking {
    val numbers: Flow<Int> = flow {
        for (i in 1..3) {
            emit(i)
        }
    }

    val newFlows: Flow<String> = numbers.map { transform(it) }
    newFlows.collect {
        println(it)
    }
}