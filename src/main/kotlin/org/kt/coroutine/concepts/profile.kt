package org.kt.coroutine.concepts

import kotlinx.coroutines.*

class Profile(val id: String)

val scope = CoroutineScope(Dispatchers.Default)

fun fetchAndLoadProfile(id: String) = scope.launch {
    val profile = fetchProfile(id) // suspend
    loadProfile(profile)
}

fun loadProfile(profile: Profile) {
    println(">>> load profile : ${profile.id}")
}

suspend fun fetchProfile(id: String): Profile = withContext(Dispatchers.IO) {
    delay(1500)
    Profile(id)
}

fun main() = runBlocking {
    val job = fetchAndLoadProfile("bond")
    job.join()
}