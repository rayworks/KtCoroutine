import kotlinx.coroutines.*

fun main(args: Array<String>) = runBlocking {

    val ceh = CoroutineExceptionHandler { _, e ->
        println("Handled $e")
    }
    var job = launch(ceh) {
        var i = 0;
        while (true) {
            println("$i I'm still working...")
            i++
//            Thread.sleep(10)
            delay(10)
        }
    }
    delay(30)
    job.cancel()

}