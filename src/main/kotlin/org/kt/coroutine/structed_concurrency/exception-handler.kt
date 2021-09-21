package org.kt.coroutine.structed_concurrency

import kotlinx.coroutines.*

// Unhandled exceptions processed by the coroutine framework :
// it tries to use a CEH if the coroutine context has one. if not, it delegates to the 'global handler'
fun main() = runBlocking {
    val ceh = CoroutineExceptionHandler { _, t ->
        println("CEH handled $t")
    }

    val scope = CoroutineScope(Job() /*+ ceh*/)
    val job = scope.launch(ceh) {
        testCoroutineScope()
//        testSupervisor(this, ceh)
    }
    job.join()
    println("Program ends")
}

private suspend fun testCoroutineScope() {
    coroutineScope {
        val task1 = launch {
            delay(100)
            println("Done background task")
        }
        val task2 = async {
            throw  Exception()
            1
        }
        task1.join()
    }
}

fun testSupervisor(coroutineScope: CoroutineScope, ceh: CoroutineExceptionHandler) =
    coroutineScope.launch {
        supervisorScope {
            val task1 = launch {
                delay(100)
                println("Done background task")
            }

            // usage of CEH on a supervisorScope direct child
            val task2 = launch(ceh) {
                throw  Exception()
                1
            }
            task1.join()
            task2.join()
        }
    }
