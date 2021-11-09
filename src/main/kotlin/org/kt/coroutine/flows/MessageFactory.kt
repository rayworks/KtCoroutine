package org.kt.coroutine.flows

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*

abstract class MessageFactory : Thread() {
    private val observers = Collections.synchronizedList(
        mutableListOf<MessageObserver>()
    )
    private var isActive = true

    fun registerObserver(observer: MessageObserver) {
        observers.add(observer)
    }

    fun unregisterObserver(observer: MessageObserver) {
        observers.removeAll { it == observer }
    }

    fun cancel() {
        isActive = false
        observers.forEach {
            it.onCancelled()
        }
        observers.clear()
    }

    override fun run() = runBlocking {
        while (isActive) {
            val message = fetchMessage()
            observers.forEach {
                it.onMessage(message)
            }
            delay(1000)
        }
    }

    abstract fun fetchMessage(): Message

    interface MessageObserver {
        fun onMessage(msg: Message)
        fun onCancelled()
        fun onError(cause: Throwable)
    }
}