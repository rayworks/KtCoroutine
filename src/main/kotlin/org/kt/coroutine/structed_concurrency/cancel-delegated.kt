package org.kt.coroutine.structed_concurrency

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

fun run() {
    val request = Request.Builder()
        .url("https://publicobject.com/helloworld.txt")
        .build()

    val client = OkHttpClient()
    client.newCall(request).execute().use { resp ->
        if (!resp.isSuccessful) {
            throw IOException("Unexpected code $resp")
        }

        for ((name, value) in resp.headers) {
            println("$name: $value")
        }
        println(resp.body?.string())
    }
}

fun main() {
    run()
}