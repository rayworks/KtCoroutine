package org.kt.coroutine.test

import kotlinx.coroutines.*
import org.junit.Test
import org.kt.coroutine.structed_concurrency.customScope
import java.io.IOException
import kotlin.test.assertFails

class CustomScopeTest {

    @Test
    fun testCustomScope(): Unit = runBlocking {
        /***
         * SupervisorJob          ← top-level, supervises its direct children
         *   └── launch (regular Job)   ← this is the only direct child of SupervisorJob
         *         ├── launch (child 1) → throws IOException
         *         └── launch (child 2) → subJob2
         *
         * When child 1 throws, the exception propagates to its parent — the outer launch's regular Job. A regular Job
         * cancels all its children when one fails. So subJob2 gets cancelled.
         * The SupervisorJob only prevents failure propagation among its direct children. Here it has only one direct child
         * (the outer launch), so it doesn't help.
         */
        var job: Job? = null
        customScope.launch {
            launch {
                delay(1000)
                throw IOException("Test exception")
            }

            job = launch {
                delay(2000)
                println("ScopeErr: child task2 in Scope is running")
            }
        }.join()

        job?.join()

        // >>> job status : cancelled - true, completed-- true
        println(">>> job status : cancelled - ${job?.isCancelled}, completed-- ${job?.isCompleted}")

        assertFails {
            assert(!job!!.isCancelled)
        }
    }

    @Test
    fun initWithCustomScope2() = runBlocking {
        var job2: Job? = null
        customScope.launch {
            delay(1000)
            throw IOException("Test exception")
        }
        job2 = customScope.launch {
            delay(2000)
            println("child task2 in Scope is running")
        }
        job2?.join()

        println(">>> job status : cancelled - ${job2?.isCancelled}, completed-- ${job2?.isCompleted}")
        assert(!job2!!.isCancelled)
    }

    @Test
    fun initWithCustomScope1() = runBlocking {
        var subJob2: Job? = null

        val outerJob = customScope.launch {
            // wrapped with a supervisorScope
            supervisorScope {
                launch {
                    delay(100)
                    throw IOException("This is an IOException")
                }

                subJob2 = launch {
                    delay(200)
                    println("child task2 is running")
                }
            }
        }

        // Wait for outer job to finish, ensuring subJob2 is assigned and completed
        outerJob.join()

        println(">>> job status : cancelled - ${subJob2?.isCancelled}, completed-- ${subJob2?.isCompleted}")
        assert(subJob2 != null)
        assert(!subJob2!!.isCancelled) // not affected, will be completed.
    }
}