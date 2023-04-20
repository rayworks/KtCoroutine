package org.kt.coroutine.concepts

import kotlinx.coroutines.*
import mu.KotlinLogging

// Code taken and modified from : Kotlin Coroutines - A Comprehensive Introduction
// https://blog.rockthejvm.com/kotlin-coroutines-101

// Note :
// You can add the VM option :  -Dkotlinx.coroutines.debug to observe the thread info
//
val logger = KotlinLogging.logger {}

@OptIn(DelicateCoroutinesApi::class)
class Playground {
    private suspend fun bathTime() {
        logger.info("Going to the bathroom")
        delay(500L)
        logger.info("Exiting the bathroom")
    }

    private suspend fun boilingWater() {
        logger.info("Boiling water")
        delay(1000L)
        logger.info("Water boiled")
    }

    suspend fun startMorningRoutine() {
        coroutineScope {
            bathTime()
        }
        coroutineScope {
            boilingWater()
        }
    }

    suspend fun concurrentMorningRoutine() { //  structural concurrency
        coroutineScope {
            launch {
                bathTime()
            }
            launch {
                boilingWater()
            }

            // it will wait until the end of the execution of both of children before returning
        }
    }

    suspend fun noStructureConcurrency() {
        GlobalScope.launch {
            bathTime()
        }
        GlobalScope.launch {
            boilingWater()
        }

        // wait the end of the execution of the two coroutines
        Thread.sleep(2000L)
    }

    ///
    private suspend fun prepareCoffee() {
        logger.info("Preparing coffee")
        delay(500L)
        logger.info("Coffee prepared")
    }

    suspend fun morningRoutineWithCoffee() {
        coroutineScope {
            val bathTimeJob: Job = launch {
                bathTime()
            }
            val boilingWaterJob = launch {
                boilingWater()
            }

            bathTimeJob.join()
            boilingWaterJob.join()

            launch {
                prepareCoffee()
            }
        }
    }

    suspend fun structureConcurrentMorningRoutineWithCoffee() {
        coroutineScope {
            coroutineScope {
                launch {
                    bathTime()
                }
                launch {
                    boilingWater()
                }
            }
            launch {
                prepareCoffee()
            }
        }
    }

    /// The async Builder
    private suspend fun prepareJavaCoffee(): String {
        logger.info("Preparing Java coffee")
        delay(500L)
        logger.info("Coffee prepared")

        return "Java coffee"
    }

    private suspend fun toastBread(): String {
        logger.info("Toasting bread")
        delay(1000L)
        logger.info("Bread toasted")

        return "Toasted bread"
    }

    suspend fun prepareBreakfast() {
        coroutineScope {
            val coffee: Deferred<String> = async { prepareJavaCoffee() }
            val bread: Deferred<String> = async { toastBread() }

            logger.info("I'm eating ${coffee.await()} and ${bread.await()}")
        }
    }

    /// Cooperative scheduling
    suspend fun workHard() {
        logger.info("Working")
        while (true) { // never yield control
            // Do nothing
        }
        delay(100L)
        logger.info("Work done")
    }

    private suspend fun workConsciousness() {
        logger.info("Working")
        while (true) {
            delay(100L)
        }
        logger.info("Work done")
    }

    private suspend fun takeABreak() {
        logger.info("Taking a break")
        delay(1000L)
        logger.info("Break done")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun workRoutine() {
        val dispatcher = Dispatchers.Default.limitedParallelism(2)
        // Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        coroutineScope {
            launch(dispatcher) {
                workConsciousness()
            }
            launch(dispatcher) {
                takeABreak()
            }
        }
    }

    /// Cancellation
    suspend fun forgetBirthdayRoutine() {
        coroutineScope {
            val work = launch { workConsciousness() }
            launch {
                delay(2000L)
                work.cancelAndJoin()

                logger.info("I forgot the birthday! Let's go to the mall!")
            }
        }
    }

    suspend fun forgetBirthdayRoutineAndCleanDesk() {
        val desk = Desk()
        coroutineScope {
            val work = launch {
                desk.use {
                    workConsciousness()
                }
            }

            launch {
                delay(2000L)

                work.cancelAndJoin()
                logger.info("I forgot the birthday! Let's go to the mall!")
            }
        }
    }

    suspend fun forgetBirthdayRoutineAndCleanDeskOnCompletion() {
        val desk = Desk()
        coroutineScope {
            val workingJob = launch {
                workConsciousness()
            }
            workingJob.invokeOnCompletion {
                desk.close()
            }

            launch {
                delay(2000L)
                workingJob.cancelAndJoin()

                logger.info("I forgot the birthday! Let's go to the mall!")
            }
        }
    }

    private suspend fun drinkWater() {
        while (true) {
            logger.info("Drinking water")
            delay(1000L)
            logger.info("Water drunk")
        }
    }

    suspend fun forgetBirthDayWhileWorkingAndDrinkingWater() {
        coroutineScope {
            val workingJob = launch {
                launch {
                    workConsciousness()
                }
                launch {
                    drinkWater()
                }
            }

            launch {
                delay(2000L)
                workingJob.cancelAndJoin() // cancel and stop its children’s coroutines
                logger.info("I forgot the birthday! Let's go to the mall!")
            }
        }
    }

    /// Coroutine Context
    suspend fun asynchronousGreeting() {
        coroutineScope {
            val context = CoroutineName("Greeting Coroutine") + Dispatchers.Default
            launch(context) {
                logger.info("Hello Everyone!")
                logger.info("Coroutine name: {}", context[CoroutineName]?.name)
            }
        }
    }

    suspend fun coroutineCtxInheritance() {
        coroutineScope {
            launch(CoroutineName("Greeting Coroutine")) {
                logger.info("Hello everyone from the outer coroutine!")
                launch {
                    logger.info("Hello everyone from the inner coroutine!")
                }
                delay(200L)
                logger.info("Hello again from the outer coroutine!")
            }
        }
    }

    suspend fun coroutineCtxOverride() {
        coroutineScope {
            launch(CoroutineName("Greeting Coroutine")) {
                logger.info("Hello everyone from the outer coroutine!")

                launch(CoroutineName("Greeting Inner Coroutine")) {
                    logger.info("Hello everyone from the inner coroutine!")
                }

                delay(200L)
                logger.info("Hello again from the outer coroutine!")
            }
        }
    }
}

class Desk : AutoCloseable {
    init {
        logger.info("Starting to work on the desk")
    }

    override fun close() {
        logger.info("Cleaning the desk")
    }
}


suspend fun main() {
    logger.info("Starting the morning routine")
    val playground = Playground()
    playground.coroutineCtxOverride()
    logger.info("Ending the morning routine")
}