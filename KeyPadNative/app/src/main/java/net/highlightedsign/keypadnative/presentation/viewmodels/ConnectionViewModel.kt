package net.highlightedsign.keypadnative.presentation.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class ConnectionViewModel: ViewModel() {
    private val _bluetoothOn = MutableStateFlow<Boolean>(false)
}