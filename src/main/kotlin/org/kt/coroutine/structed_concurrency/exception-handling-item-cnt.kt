package org.kt.coroutine.structed_concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.System.currentTimeMillis

fun main() = runBlocking {
    val itemCntDeferred = async {
        try {
            getItemCount()
        } catch (e: Exception) {
            // sth went wrong
            0
        }
    }
    val count = itemCntDeferred.await()
    println("Item count : $count")

    resultOrException(this)

    return@runBlocking
}

fun getItemCount(): Int {
    throw Exception()
}

fun resultOrException(coroutineScope: CoroutineScope) = coroutineScope.launch {
    val result = kotlin.runCatching {
        val flag = currentTimeMillis().mod(2) == 0
        if (flag) {
            42
        } else {
            throw  IllegalStateException("State Exception")
        }
    }
    if (result.isSuccess) {
        println(result.getOrNull())
    } else {
        println(result.exceptionOrNull())
    }
}