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

## Suspend

* Suspending functions (with modifier `suspend` and injected with
  a [hidden parameter](https://github.com/Kotlin/KEEP/blob/master/proposals/coroutines.md#continuation-passing-style)
  `Continuation` by the compiler) are
  like [state machines](https://github.com/Kotlin/KEEP/blob/master/proposals/coroutines.md#state-machines), with a
  possible state at the beginning of the function and
  after each suspending function call.

* The labels identifying the state and the local data are kept in the `Continuation` object.

* `Continuation` of one function decorates a continuation of its caller function; as a result, all these continuations
  represent a call stack that is used when we resume or a resumed function completes.

* Kotlin Coroutines allow suspending without blocking the thread through a technique called
  ["cooperative multitasking.](https://github.com/Kotlin/KEEP/blob/master/proposals/coroutines.md#cooperative-single-thread-multitasking)"
  When a coroutine encounters a suspension point, it voluntarily suspends its execution, allowing other coroutines to
  proceed. This suspension is non-blocking because it doesn't occupy a thread while waiting.
