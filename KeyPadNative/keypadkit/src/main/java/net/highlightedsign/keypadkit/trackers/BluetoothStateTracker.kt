package net.highlightedsign.keypadkit.trackers

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow

class BluetoothStateTracker(context: Context, scope: CoroutineScope) :
    BroadcastFlowTracker<Int>(
        context,
        scope,
        MutableSharedFlow(getInitialState(context))
    ){

    override fun getIntentActions(): List<String> =
        listOf(BluetoothAdapter.ACTION_STATE_CHANGED)

    override fun handleBroadcast(intent: Intent) {
        if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED){
            val newState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
            _flow.tryEmit(newState)
        }
    }

    companion object {
        fun getInitialState(context: Context): Int {
            val bluetoothManager =
                context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
            return bluetoothManager?.adapter?.state ?: BluetoothAdapter.STATE_OFF
        }
    }
}