# Queued Adapter

Implementation of an RecyclerView.Adapter for easy handling multiple
updates.

It make use of the [DiffUtil](https://developer.android.com/reference/android/support/v7/util/DiffUtil)
and Kotlin coroutines for handling the update.

Right know it use a simple **Last in first out** as update strategy and it's not
configurable. Maybe I'll add it up the option of multiple update strategy