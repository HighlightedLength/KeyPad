package net.highlightedsign.keypadkit.models

sealed class DeviceConnectionStatus {
    object Idle : DeviceConnectionStatus()
    object Connecting : DeviceConnectionStatus() // <-- Application state
    object Connected : DeviceConnectionStatus()  // <-- Confirmed by ACL broadcast
    object Disconnecting : DeviceConnectionStatus() // <-- Application state
    object Disconnected : DeviceConnectionStatus() // <-- Confirmed by ACL broadcast
    class Failed(val reason: String) : DeviceConnectionStatus()
}