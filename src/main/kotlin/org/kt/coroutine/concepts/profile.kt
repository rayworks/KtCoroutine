package org.kt.coroutine.concepts

import kotlinx.coroutines.*
import org.kt.coroutine.utils.log

class Profile(val id: String)

val scope = CoroutineScope(Dispatchers.Default)

fun fetchAndLoadProfile(id: String) = scope.launch {
    val profile = fetchProfile(id) // suspend
    loadProfile(profile)
}

fun loadProfile(profile: Profile) {
    log(">>> load profile : ${profile.id}")
}

suspend fun fetchProfile(id: String): Profile = withContext(Dispatchers.IO) {
    log(">>> Before delay")
    delay(1500)
    log(">>> End delay")
    Profile(id)
}

fun main() = runBlocking {
    val job = fetchAndLoadProfile("bond")
    log(">>> Before join")
    job.join()
    log(">>> End of main")
}
