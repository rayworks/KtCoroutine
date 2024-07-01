## Summary

### Flow VS. LiveData

|                          | Flow | LiveData       |
|--------------------------|------|----------------|
| Observable data holder   | ✅    | ✅              |
| Backpressure             | ✅    | ❌              |
| Across platforms         | ✅    | ❌（part of AAC） |
| LifeCycle-aware          | ❌    | ✅              |
| Transforming data        | ✅    | ❌              |
| Execution context switch | ✅    | ❌              |

### StateFlow and SharedFlow

* ColdFlow : won't produce values until one starts to collect them.
* HotFlow : produces values even if no one is collecting them.

The `ColdFlow` could be converted to a hot one using the `stateIn()` and `shareIn()` operators respectively.

A `SharedFlow` is a `HotFlow` that can have multiple collectors.
It's useful when there are multiple receivers for the same stream of data.

A `StateFlow` is a `SharedFlow` which holds a single value at a time. It always has an initial value and stores the
latest emitted value. It replays the last state for new subscribers and only notifies subscribers when the state
changes.



