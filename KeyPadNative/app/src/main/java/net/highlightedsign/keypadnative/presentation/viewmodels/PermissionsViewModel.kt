package net.highlightedsign.keypadnative.presentation.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


class PermissionsViewModel(initialPermissions: Map<String, Boolean>) : ViewModel() {
    val permissions: Map<String, MutableState<Boolean>> =
        initialPermissions.mapValues{ mutableStateOf(it.value) }

    fun setPermission(permission: String, value: Boolean) {
        permissions[permission]?.value = value
    }

    var checkAllPermissionsGranted = { permissions.values.all { it.value } }
}
