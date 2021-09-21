# KtCoroutine

The selected source code to demonstrate [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) in the
book [《Programming Android with Kotlin : Achieving Structured Concurrency with Coroutines》](https://www.oreilly.com/library/view/programming-android-with/9781492062998/)
.

The perspectives on Kotlin Coroutines are really attractive to me.

> “You may conceptualize Coroutines as blocks of code that can be dispatched to threads that are non-blocking.”

> “Coroutines are indeed lightweight, but it is important to note that coroutines aren’t threads themselves...... they
> really are just state machines, with each state corresponding to a block of code that some thread will eventually
> execute.”

And the problems Kotlin Coroutines are trying to resolve including :

> 1. Exceptions aren’t propagated
> 2. Execution flow is hard to control

In the book, author also compares 2 different methods to tackle the problems (the traditional one and the one with
Coroutines) which makes the transition to Coroutines smoothly.

Will update the notes and code as my reading continues...