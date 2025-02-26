package org.kt.coroutine.structed_concurrency

import kotlinx.coroutines.*


val startTime = System.currentTimeMillis()

fun main() = runBlocking {
    withContext(Dispatchers.Unconfined) {
        var job = launchTimedJob()
        delay(2500)
        println("main : I'm going to cancel the job")
        job.cancel()

        println("after cancel : completed - ${job.isCompleted}, " +
                "cancelled - ${job.isCancelled}" + " active : ${job.isActive}")
        if (!job.isActive) {
            println("restarting job!")


            job = launchTimedJob()
            println("is completed - ${job.isCompleted}")
            if (job.isActive) {
                println("activated job!")
                job.start()
            }
            delay(2000)
            job.cancelAndJoin()
        }

        println("main : Done")
    }
}

private fun CoroutineScope.launchTimedJob() = launch(Dispatchers.Default) {
    var nextPrintTime = startTime
    while (true) { // child coroutine is still running despite the cancellation from the parent
        if (System.currentTimeMillis() >= nextPrintTime) {
            log("job : I'm working...")
            nextPrintTime += 500

            // however, functions from the standard library already support cancellation.
            //delay(10)
            ensureActive()

            log("job : I'm working on next task ...")
        }
    }
}
