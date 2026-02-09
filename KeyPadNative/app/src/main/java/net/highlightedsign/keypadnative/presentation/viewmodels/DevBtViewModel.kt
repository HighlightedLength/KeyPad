package net.highlightedsign.keypadnative.presentation.viewmodels

import android.bluetooth.BluetoothAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import net.highlightedsign.keypadkit.KeyPadBtManager

class DevBtViewModel(private val keyPadBtManager: KeyPadBtManager) : ViewModel() {
    val adapterState = keyPadBtManager.adapterState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            BluetoothAdapter.STATE_OFF
        )
}