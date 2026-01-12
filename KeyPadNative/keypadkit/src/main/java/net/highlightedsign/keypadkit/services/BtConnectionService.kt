package net.highlightedsign.keypadkit.services

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.highlightedsign.keypadkit.interfaces.IBtConnectionService
import net.highlightedsign.keypadkit.models.AclConnectionState
import net.highlightedsign.keypadkit.models.DeviceConnectionStatus
import net.highlightedsign.keypadkit.trackers.BluetoothStateTracker
import net.highlightedsign.keypadkit.trackers.BondStateTracker
import net.highlightedsign.keypadkit.trackers.ConnectionStateTracker
import java.io.IOException
import java.util.UUID

class BtConnectionService (
    private val context: Context,
    private val bluetoothAdapter: BluetoothAdapter
) : IBtConnectionService {
    // --- Coroutine Infrastructure (The Scope) ---
    private val repositoryJob = SupervisorJob()
    // Dispatchers.IO for blocking socket operations
    private val repositoryScope = CoroutineScope(Dispatchers.IO + repositoryJob)
    // ------------------------------------------

    // --- 1. Trackers ---
    private val bluetoothStateTracker = BluetoothStateTracker(context, repositoryScope)
    private val bondStateTracker = BondStateTracker(context, repositoryScope)
    private val connectionStateTracker = ConnectionStateTracker(context, repositoryScope)

    // --- 2. StateFlows (Exposed to ViewModel) ---

    // High-level connection status (the FINAL state)
    private val _connectionStatus = MutableStateFlow<DeviceConnectionStatus>(DeviceConnectionStatus.Idle)
    override val connectionStatus: StateFlow<DeviceConnectionStatus> = _connectionStatus.asStateFlow()

    // Persistent target address
    private val _targetDeviceAddress = MutableStateFlow<String?>(null)
    override val targetDeviceAddress: StateFlow<String?> = _targetDeviceAddress.asStateFlow()

    // Bonded devices list
    private val _bondedDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    override val bondedDevices: StateFlow<List<BluetoothDevice>> = _bondedDevices.asStateFlow()

    // Bluetooth ON/OFF status
    override val isBluetoothOn: StateFlow<Boolean> = bluetoothStateTracker.flow.map { state ->
        state == BluetoothAdapter.STATE_ON
    }.stateIn(
        repositoryScope,
        SharingStarted.Eagerly,
        BluetoothAdapter.STATE_ON == BluetoothStateTracker.getInitialState(context)
    )

    // --- Internal State ---
    private var currentConnectionJob: Job? = null
    private var currentSocket: BluetoothSocket? = null


    init {
        // Start collection of tracker flows immediately
        // A. Bond State: Listen for changes to refresh the bonded list
        bondStateTracker.flow
            .onEach { refreshBondedDevices() }
            .launchIn(repositoryScope)

        // B. Connection ACL State: Listen for connection/disconnection confirmation
        connectionStateTracker.flow
            .onEach { aclState -> handleAclLinkUpdate(aclState) }
            .launchIn(repositoryScope)

        // Initial load
        refreshBondedDevices()
    }

    @SuppressLint("MissingPermission")
    private fun refreshBondedDevices() {
        // Must be called from a safe thread (main is fine for getBondedDevices)
        val currentBonded = bluetoothAdapter.bondedDevices ?: emptySet()
        _bondedDevices.value = currentBonded.toList()
    }

    private fun handleAclLinkUpdate(aclState: AclConnectionState) {
        val targetAddress = _targetDeviceAddress.value

        // Only react to the device we care about
        if (aclState.device.address != targetAddress) return

        when (aclState) {
            is AclConnectionState.Connected -> {
                // If the ACL is connected, and we are CONNECTING, assume success
                if (_connectionStatus.value is DeviceConnectionStatus.Connecting) {
                    _connectionStatus.value = DeviceConnectionStatus.Connected
                }
            }
            is AclConnectionState.Disconnected -> {
                // The system has confirmed the link is down
                cleanUpSocket()
                _connectionStatus.value = DeviceConnectionStatus.Disconnected
            }
        }
    }

    // --- Public API ---
    override fun setTargetDevice(device: BluetoothDevice) {
        _targetDeviceAddress.value = device.address
        // Clear old connection status if a new device is selected
        _connectionStatus.value = DeviceConnectionStatus.Idle
    }

    @SuppressLint("MissingPermission")
    // TODO: check permissions from a permissions repository
    override fun attemptConnection() {
        val address = _targetDeviceAddress.value ?: return

        // 1. Cancel any previous attempt
        currentConnectionJob?.cancel()
        currentConnectionJob = repositoryScope.launch {
            _connectionStatus.value = DeviceConnectionStatus.Connecting

            try {
                val device = bluetoothAdapter.getRemoteDevice(address)
                // 2. Create the socket (use the appropriate UUID for HID profile)
                val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // RFCOMM Example

                currentSocket = device.createRfcommSocketToServiceRecord(uuid)

                // 3. Blocking call to connect
                currentSocket?.connect()

                // 4. On success, the ACTION_ACL_CONNECTED intent should follow
                // If it doesn't, we wait for a brief period before setting to Connected
                delay(500) // Give the broadcast system time to send the intent
                if (_connectionStatus.value == DeviceConnectionStatus.Connecting) {
                    _connectionStatus.value = DeviceConnectionStatus.Connected
                }

                // 5. Start a continuous read/write stream handler here (I/O management)

            } catch (e: IOException) {
                // Connection failed (timeout, refusal, etc.)
                Log.e("BT_REPO", "Connection failed: ${e.message}")
                cleanUpSocket()
                _connectionStatus.value = DeviceConnectionStatus.Failed(e.message ?: "Connection error")
            }
        }
    }

    private fun cleanUpSocket() {
        try {
            currentSocket?.close()
        } catch (e: IOException) {
            // Log failure to close, but proceed
        }
        currentConnectionJob?.cancel()
        currentConnectionJob = null
        currentSocket = null
    }

    // --- Mandatory Cleanup ---
    override fun cleanup() {
        cleanUpSocket()
        bluetoothStateTracker.stopTracking()
        bondStateTracker.stopTracking()
        connectionStateTracker.stopTracking()
        repositoryJob.cancel() // Cancel the parent job to stop ALL coroutines
    }
}