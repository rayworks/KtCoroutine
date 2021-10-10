package org.kt.coroutine.channels

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val items = getItems()
    consume(items)
}

suspend fun getItems(): List<Item> {
    val items = mutableListOf<Item>()
    items.add(makeItem())
    items.add(makeItem())
    items.add(makeItem())
    return items
}

fun consume(items: List<Item>) {
    for (item in items)
        println("Do something with $item")
}

suspend fun makeItem(): Item {
    delay(10) // simulate some asynchronism
    return Item()
}
