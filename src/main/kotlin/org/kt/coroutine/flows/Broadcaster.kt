package org.kt.coroutine.flows

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds

class Broadcaster {
    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages = _messages.asSharedFlow()

    fun beginBroadcasting(scope: CoroutineScope) {
        scope.launch {
            _messages.update {
                it + "Hello"
            }
            _messages.update {
                it + "Hi"
            }
            _messages.update {
                it + "Hola"
            }
        }
    }
}

fun main(): Unit = runBlocking {
    val broadcaster = Broadcaster()
    broadcaster.beginBroadcasting(scope = this)
    delay(200.milliseconds)

    broadcaster.messages.collect {
        println("message : $it")
    }
}