package net.highlightedsign.keypadkit.models

import android.bluetooth.BluetoothAdapter

enum class AdapterState(val value: Int) {
    OFF(BluetoothAdapter.STATE_OFF),
    ON(BluetoothAdapter.STATE_ON),
    TURNING_ON(BluetoothAdapter.STATE_TURNING_ON),
    TURNING_OFF(BluetoothAdapter.STATE_TURNING_OFF);

    companion object {
        fun getByValue(value: Int) =
            AdapterState.entries.find { it.value == value } ?: OFF

    }
}