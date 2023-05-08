package org.kt.coroutine.flows

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


data class DownloadEvent(val url: String)

class EventBus {
    private val _innerFlow = MutableSharedFlow<DownloadEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val flow = _innerFlow as SharedFlow<DownloadEvent>

    fun fireDownloadStarted(url: String) = _innerFlow.tryEmit(DownloadEvent(url))
}

class Downloader(private val eventBus: EventBus, scope: CoroutineScope) {

    init {
        scope.launch {
            logger.info("subscribed - at ${System.currentTimeMillis()}")
            eventBus.flow.collect {
                download(it.url)
            }
        }
    }

    private fun download(url: String) {
        logger.info("Downloading $url now")
    }
}

fun main() = runBlocking {
    val bus = EventBus()

    logger.info("start download")
    Downloader(bus, this)

    delay(100)

    logger.info("fire at ${System.currentTimeMillis()}")
    bus.fireDownloadStarted("http://some_link#")

    Unit
}