package net.highlightedsign.keypadkit.interfaces

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.StateFlow
import net.highlightedsign.keypadkit.models.DeviceConnectionStatus

interface IBtConnectionService {
    val connectionStatus: StateFlow<DeviceConnectionStatus>
    val targetDeviceAddress: StateFlow<String?>
    val bondedDevices: StateFlow<List<BluetoothDevice>>
    val isBluetoothOn: StateFlow<Boolean>

    fun setTargetDevice(device: BluetoothDevice)
    fun attemptConnection()
    fun cleanup()
}