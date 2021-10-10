package org.kt.coroutine.channels

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class Item(val number: Int = 0)

fun main() = runBlocking {
    val channel = Channel<Item>()
    launch {
        channel.send(Item(1))
        channel.send(Item(2))
        println("Done sending")

//        channel.close()
    }

    // closing the channel will break the loop on channel
//    for (x in channel) {
//        println(x)
//    }
    println(channel.receive())
    println(channel.receive())

    // keep waiting if one more call to receive
//    println(channel.receive())

    println("Done!")
}