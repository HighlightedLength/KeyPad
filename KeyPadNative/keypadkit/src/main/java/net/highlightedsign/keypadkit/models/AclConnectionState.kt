package net.highlightedsign.keypadkit.models

import android.bluetooth.BluetoothDevice

sealed class AclConnectionState(val device: BluetoothDevice) {
    // Each state is a subclass that carries the device object
    class Connected(device: BluetoothDevice) : AclConnectionState(device)
    class Disconnected(device: BluetoothDevice) : AclConnectionState(device)
}