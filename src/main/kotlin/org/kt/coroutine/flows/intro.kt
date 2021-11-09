package org.kt.coroutine.flows

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

fun members(): Flow<Int> = flow {
    emit(1)
    emit(2)
}

@InternalCoroutinesApi
fun main() = runBlocking {
    val flow = members()
    flow.collect(object : FlowCollector<Int> {
        override suspend fun emit(value: Int) {
            println("$value")
        }
    })
}