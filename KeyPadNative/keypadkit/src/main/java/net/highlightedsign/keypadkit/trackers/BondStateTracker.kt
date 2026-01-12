package net.highlightedsign.keypadkit.trackers

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

class BondStateTracker(context: Context, scope: CoroutineScope) :
    BroadcastFlowTracker<Unit>(
        context,
        scope,
        MutableSharedFlow(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST)
    ) {

    // 1. Define the actions to listen for
    override fun getIntentActions(): List<String> =
        listOf(BluetoothDevice.ACTION_BOND_STATE_CHANGED)

    // 2. Implement the specific handling logic
    override fun handleBroadcast(intent: Intent) {
        val newState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR)

        // Signal the repository to refresh the bonded device list
        if (newState == BluetoothDevice.BOND_BONDED || newState == BluetoothDevice.BOND_NONE) {
            _flow.tryEmit(Unit)
        }
    }
}