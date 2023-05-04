package org.kt.coroutine.flows

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import mu.KotlinLogging

val logger = KotlinLogging.logger {}

interface Callback {
    fun onData(str: String?)

    fun dispose()
}

open class MockWebView {
    private var callback: Callback? = null

    fun register(cb: Callback) {
        logger.info("cb registered")
        callback = cb
    }

    fun unregister() {
        logger.info("cb unregistered")
        callback = null
    }

    fun update(str: String) {
        callback?.onData(str)
    }

    fun destroy() {
        callback?.dispose()
    }
}

fun getMsgFlow(webView: MockWebView) = callbackFlow {
    val callback = object : Callback {
        override fun onData(str: String?) {
            logger.info("Received $str from Web")
            trySend(str)
        }

        override fun dispose() {
            logger.info("Callback disposed")
            channel.close()
        }
    }

    webView.register(callback)

    awaitClose { webView.unregister() }
}

fun main() = runBlocking {
    val webView = MockWebView()

    val deferred = async {
        // simulate async calls
        delay(3000)

        webView.update("test")
        delay(2000)
        webView.update("alpha-beta")
        delay(1000)

        logger.info(">>><<< about to destroy WebView")
        webView.destroy()
    }

    getMsgFlow(webView).collect {
        logger.info("Collected : $it")
    }

    deferred.await()
    logger.info("End of Main()")
}