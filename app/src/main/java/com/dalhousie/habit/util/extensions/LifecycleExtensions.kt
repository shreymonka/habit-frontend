package com.dalhousie.habit.util.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.dalhousie.habit.util.Event
import com.dalhousie.habit.util.EventObserver

/**
 * Extension function for observing [LiveData] containing [Event]
 * @param owner is [LifecycleOwner] which will be used to listen lifecycle changes
 * @param func is a function which will be executed whenever [LiveData] is changed
 */
fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, func: (T) -> Unit) =
    observe(owner, EventObserver {
        func(it)
    })