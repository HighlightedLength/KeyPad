package net.highlightedsign.keypadnative.locallib

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

class BtManager (
    private val context: Context // must be application context from Application
){

    private val bluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val adapter: BluetoothAdapter? = bluetoothManager.adapter

    // expose whether adapter is on/off as a flow
    val adapterState: Flow<Int> = callbackFlow {
        // create a receiver that captures the state of the BluetoothAdapter
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                    val currentState = intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.STATE_OFF
                    )
                    trySend(currentState)
                }
            }
        }

        // register the receiver to receive the state changes from the context
        context.registerReceiver(
            receiver,
            IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        )

        // suspend until the flow is closed
        awaitClose {
            // unregister the listener to prevent memory leaks
            context.unregisterReceiver(receiver)
        }
    }
    .onStart {
        val currentState = adapter?.state ?: BluetoothAdapter.STATE_OFF
        emit(currentState)
    }
}