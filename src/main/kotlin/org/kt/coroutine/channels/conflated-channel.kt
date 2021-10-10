package org.kt.coroutine.channels

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val channel = Channel<String>(Channel.CONFLATED) // a buffer of size 1
    val job = launch {
        channel.send("one")
        channel.send("two")
    }
    job.join()

    val elem = channel.receive()
    println("Last value was : $elem") // two
}