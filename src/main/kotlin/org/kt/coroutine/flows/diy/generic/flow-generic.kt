package org.kt.coroutine.flows.diy.generic

/***
 * Code taken from :
 * Flow under the hood: how does it really work（https://kt.academy/article/how-flow-works）
 */
fun interface FlowCollector<T> { // a functional interface
    suspend fun emit(value: T)
}

interface Flow<T> { // interface Flow
    suspend fun collect(collector: FlowCollector<T>)
}

fun <T> flow(builder: suspend FlowCollector<T>.() -> Unit) = object : Flow<T> {
    override suspend fun collect(collector: FlowCollector<T>) {
        collector.builder()
    }
}

suspend fun main() {

    val f = flow<String> {
        emit("A")
        emit("B")
        emit("C")
    }
    f.collect { print(it) }
    f.collect { print(it) }
}

fun <T> Iterable<T>.asFlow(): Flow<T> = flow {
    forEach { value ->
        emit(value)
    }
}

fun <T> Sequence<T>.asFlow(): Flow<T> = flow {
    forEach { value ->
        emit(value)
    }
}

fun <T> flowOf(vararg elements: T): Flow<T> = flow {
    for (elem in elements) {
        emit(elem)
    }
}

// flow operations
fun <T, R> Flow<T>.map(transformation: suspend (T) -> R): Flow<R> = flow {
    collect {
        emit(transformation(it))
    }
}

fun <T> Flow<T>.filter(predicate: suspend (T) -> Boolean): Flow<T> = flow {
    collect {
        if (predicate(it))
            emit(it)
    }
}

fun <T> Flow<T>.onEach(action: suspend (T) -> Unit): Flow<T> = flow {
    collect {
        action(it)
        emit(it)
    }
}

// simplified implementation
fun <T> Flow<T>.onStart(action: suspend () -> Unit): Flow<T> = flow {
    action()
    collect {
        emit(it)
    }
}


