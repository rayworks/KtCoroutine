package org.kt.coroutine.concepts

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking<Unit> {
        launch(Dispatchers.Default) {
            println("I'm executing in ${Thread.currentThread().name} ")
        }
    }
}