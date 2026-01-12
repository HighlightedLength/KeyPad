package net.highlightedsign.keypadkit.trackers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BroadcastFlowTracker<T>(
    protected val context: Context,
    protected val scope: CoroutineScope,
    // The flow that the specific subclass will use to emit data
    protected val _flow: MutableSharedFlow<T>
) {
    // Public read-only flow for the repository to collect
    val flow: SharedFlow<T> = _flow.asSharedFlow()

    // The abstract receiver where subclasses will implement the onReceive logic
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            handleBroadcast(intent)
        }
    }

    // Subclasses must define the actions they listen for
    protected abstract fun getIntentActions(): List<String>

    // Subclasses must define how to process the received intent
    protected abstract fun handleBroadcast(intent: Intent)

    init {
        val filter = IntentFilter().apply {
            getIntentActions().forEach { action -> addAction(action) }
        }
        context.registerReceiver(receiver, filter)
    }

    // Mandatory cleanup function
    fun stopTracking() {
        context.unregisterReceiver(receiver)
        scope.cancel()
    }
}