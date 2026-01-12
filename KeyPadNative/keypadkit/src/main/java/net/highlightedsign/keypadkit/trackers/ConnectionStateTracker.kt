package net.highlightedsign.keypadkit.trackers

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import net.highlightedsign.keypadkit.models.AclConnectionState

class ConnectionStateTracker(context: Context, scope: CoroutineScope):
    BroadcastFlowTracker<AclConnectionState>(
        context,
        scope,
        MutableSharedFlow()
    ) {
    override fun getIntentActions(): List<String> = listOf(
        BluetoothDevice.ACTION_ACL_CONNECTED,
        BluetoothDevice.ACTION_ACL_DISCONNECTED
    )

    override fun handleBroadcast(intent: Intent) {
        val device: BluetoothDevice =
            intent.getParcelableExtra(
                BluetoothDevice.EXTRA_DEVICE,
                BluetoothDevice::class.java) ?: return

        // Use a 'when' statement to match the string action
        val state: AclConnectionState? = when (intent.action) {
            BluetoothDevice.ACTION_ACL_CONNECTED ->
                AclConnectionState.Connected(device)

            BluetoothDevice.ACTION_ACL_DISCONNECTED ->
                AclConnectionState.Disconnected(device)

            else -> null // Should not happen given the IntentFilter
        }

        // Emit the type-safe object
        if (state != null) {
            _flow.tryEmit(state)
        }
    }
}