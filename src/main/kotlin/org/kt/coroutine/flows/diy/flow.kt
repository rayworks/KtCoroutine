package org.kt.coroutine.flows.diy

/***
 * Code taken from :
 * Flow under the hood: how does it really work（https://kt.academy/article/how-flow-works）
 */
fun main() {
    flowV4()
}

fun interface FlowCollector { // a functional interface
    fun emit(value: String)
}

interface Flow { // interface Flow
    fun collect(collector: FlowCollector)
}

private fun flowV4() {
    val builder: FlowCollector.() -> Unit = {
        emit("A")
        emit("B")
        emit("C")
    }
    val flow: Flow = object : Flow {
        override fun collect(collector: FlowCollector) {
            collector.builder()
        }
    }
    flow.collect { print(it) }
    flow.collect { print(it) }
}

private fun flowV3() {
    val f: FlowCollector.() -> Unit = {// make FlowCollector a receiver
        emit("A")
        emit("B")
        emit("C")
    }
    f { print(it) }
    f { print(it) }
}

private fun flowV2() {
    val f: (FlowCollector) -> Unit = {
        it.emit("A")
        it.emit("B")
        it.emit("C")
    }
    f { print(it) }
    f { print(it) }
}

private fun flowV1() {
    val f: ((String) -> Unit) -> Unit = { emit ->
        emit("A")
        emit("B")
        emit("C")
    }
    f { print(it) }
    f { print(it) }
}

private fun flowV0() {
    val f: () -> Unit = {
        print("A")
        print("B")
        print("C")
    }
    f()
    f()
}