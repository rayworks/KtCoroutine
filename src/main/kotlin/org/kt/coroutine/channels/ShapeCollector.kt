package org.kt.coroutine.channels

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.selects.select
import kotlin.random.Random

data class Location(val x: Int, val y: Int)
class ShapeData
data class Shape(val location: Location, val data: ShapeData)

var count = 0

fun main() = runBlocking<Unit> {
    val shapes = Channel<Shape>()
    val locations = Channel<Location>()

    with(ShapeCollector(4)) {
        start(locations, shapes)
        consumeShapes(shapes)
    }
    sendLocations(locations)
}

fun CoroutineScope.sendLocations(locations: SendChannel<Location>) = launch {
    withTimeoutOrNull(3000) {
        while (true) {
            // simulate fetching some shape location
            val location = Location(Random.nextInt(), Random.nextInt())
            println("Sending a new location")
            locations.send(location) // suspending call
        }
    }
    println("Received $count shapes")
}

fun CoroutineScope.consumeShapes(shapes: ReceiveChannel<Shape>) = launch {
    for (shape in shapes) {
        count++
    }
}

class ShapeCollector(private val workerCount: Int) {
    fun CoroutineScope.start(locations: ReceiveChannel<Location>, shapesOutput: SendChannel<Shape>) {
        val locationToProcess = Channel<Location>()
        val locationProcessed = Channel<Location>(capacity = 1)

        repeat(workerCount) {
            worker(locationToProcess, locationProcessed, shapesOutput)
        }

        collectShapes(locations, locationToProcess, locationProcessed)
    }

    private fun CoroutineScope.worker(
        locationToProcess: ReceiveChannel<Location>,
        locationProcessed: SendChannel<Location>,
        shapesOutput: SendChannel<Shape>
    ) = launch(Dispatchers.IO) {
        for (loc in locationToProcess) {
            try {
                val data = getShapeData(loc)
                val shape = Shape(loc, data)
                shapesOutput.send(shape)
            } finally {
                locationProcessed.send(loc)
            }
        }
    }

    private fun CoroutineScope.collectShapes(
        locations: ReceiveChannel<Location>,
        locationToProcess: SendChannel<Location>, locationProcessed: ReceiveChannel<Location>
    ) = launch(Dispatchers.Default) {
        val locationBeingProcessed = mutableListOf<Location>()

        while (true) {
            select<Unit> {
                locationProcessed.onReceive {
                    locationBeingProcessed.remove(it)
                }

                locations.onReceive {
                    if (!locationBeingProcessed.any { location ->
                            location == it
                        }) {
                        locationBeingProcessed.add(it)

                        locationToProcess.send(it)
                    }
                }
            }
        }
    }

    private suspend fun getShapeData(loc: Location): ShapeData = withContext(Dispatchers.IO) {
        // simulate some remote API delay
        delay(500)
        ShapeData()
    }
}