package org.kt.coroutine.structed_concurrency

import kotlinx.coroutines.*

class Weather(val wind: Wind, val temperatures: Temperatures)

data class Hike(val name: String, val miles: Float, val accentInFeet: Int)
class Temperatures
class Wind

private suspend fun weatherForHike(hike: Hike): Weather =
    coroutineScope {
        val deferredWind = async(Dispatchers.IO) { fetchWind(hike) }
        val deferredTemp = async(Dispatchers.IO) { fetchTemperatures(hike) }

        val wind = deferredWind.await()
        val temp = deferredTemp.await()

        println("Weather constructed")
        Weather(wind, temp)
    }


suspend fun fetchTemperatures(hike: Hike): Temperatures {
    delay(500)
    return Temperatures()
}

suspend fun fetchWind(hike: Hike): Wind {
    delay(600)
    return Wind()
//    throw IllegalStateException()
}

fun main() = runBlocking {
    weatherForHike(Hike("", 0F, 0))
    return@runBlocking
}