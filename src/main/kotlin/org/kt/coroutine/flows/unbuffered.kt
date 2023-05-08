package org.kt.coroutine.flows

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val sharedFlow = MutableSharedFlow<String>()

    launch {
        sharedFlow.collect {
            logger.info("subscriber1 recved : $it")
        }
    }

    launch {
        sharedFlow.collect {
            logger.info("subscriber2 recved : $it")
            delay(3000)
        }
    }

    launch {
        sharedFlow.emit("one")
        sharedFlow.emit("two")
        sharedFlow.emit("ten")
    }

    Unit
}