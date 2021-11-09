package org.kt.coroutine.flows

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime

data class Message(val user: String, val date: LocalDateTime, val content: String) {
    suspend fun translate(language: String): Message = withContext(Dispatchers.Default) {
        copy(content = "translated content")
    }
}

lateinit var factory: MessageFactory

@ExperimentalCoroutinesApi
fun getMessagesFromUser(user: String, language: String): Flow<Message> {
    return getMessageFlow(factory)
        .filter { it.user == user }
        .map { it.translate(language) }
        .flowOn(Dispatchers.Default)
}

fun getMessageFlow(): Flow<Message> = flow {
    emit(Message("Amanda", LocalDateTime.now(), "First msg"))
    emit(Message("Amanda", LocalDateTime.now(), "Second msg"))
    emit(Message("Pierre", LocalDateTime.now(), "First msg"))
    emit(Message("Amanda", LocalDateTime.now(), "Third msg"))
}

// Use case #1: Interface with a callback-based API
@ExperimentalCoroutinesApi
fun getMessageFlow(factory: MessageFactory): Flow<Message> = callbackFlow {
    /*
     1. Instantiate the "callback". In this case, it's an observer
     2. Register that callback using the available api.
     3. Listen for close event using `awaitClose`, and provide a
        relevant action to take in this case. Most probably,
        you'll have to unregister the callback.
     */
    val observer = object : MessageFactory.MessageObserver {
        override fun onMessage(msg: Message) {
            println("Recv $msg")
            trySend(msg)
        }

        override fun onCancelled() {
            channel.close()
        }

        override fun onError(cause: Throwable) {
            cancel(CancellationException("Message factory error", cause))
        }
    }

    factory.registerObserver(observer)
    awaitClose { factory.unregisterObserver(observer) }
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
fun main() = runBlocking {
    factory = object : MessageFactory() {
        override fun fetchMessage(): Message {
            return Message("Amanda", LocalDateTime.now(), "message from user")
        }
    }
    factory.start()

    getMessagesFromUser("Amanda", "en-us")
        .collect(object : FlowCollector<Message> {
            override suspend fun emit(value: Message) {
                println("Received message from ${value.user}: ${value.content}")
            }
        })
}