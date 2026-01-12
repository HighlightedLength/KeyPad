package net.highlightedsign.keypadnative.presentation.viewmodels

import android.bluetooth.BluetoothAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import net.highlightedsign.keypadnative.locallib.BtManager

class DevBtViewModel(private val btManager: BtManager) : ViewModel() {
    val adapterState = btManager.adapterState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            BluetoothAdapter.STATE_OFF
        )
}