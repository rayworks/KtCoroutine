package org.kt.coroutine.flows

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

suspend fun transform(i: Int): String = withContext(Dispatchers.Default) {
    delay(10)

    logger.info(" $i transforming now")
    "${i + 1}"
}

@OptIn(FlowPreview::class)
fun main() = runBlocking {
    val numbers: Flow<Int> = flow {
        for (i in 1..10) {
            emit(i)
        }
    }

    val newFlows: Flow<String> = numbers.map { n ->
        flow {
            emit(transform(n))
        }
    }//.flattenConcat()
        .flattenMerge(4) // executed concurrently, the order of elements is not preserved

    val list = newFlows.toList()
    println("out : $list")
}