package org.kt.coroutine.utils

fun log(msg: String) {
    println(msg + " on Thread : ${Thread.currentThread().name}")
}