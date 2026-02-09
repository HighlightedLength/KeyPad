package net.highlightedsign.keypadkit

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import net.highlightedsign.keypadkit.models.AdapterState
import net.highlightedsign.keypadkit.models.DesiredConnectionState

class KeyPadBtManager(
    private val context: Context  // must be application context from Application
) {
    private val bluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private var adapter: BluetoothAdapter? = bluetoothManager.adapter
    private val _desiredDeviceAddress = MutableStateFlow<String?>(null);
    val deviceAddress = _desiredDeviceAddress.asStateFlow();

    private val _desiredConnectionState: MutableStateFlow<DesiredConnectionState> = MutableStateFlow(
        DesiredConnectionState.Disconnected);
    val desiredConnectionState = _desiredConnectionState.asStateFlow();

    private val _adapterState = MutableStateFlow<AdapterState>(AdapterState.OFF)
    val adapterState = _adapterState.asStateFlow()


    private val _isDiscoveringDesired = MutableStateFlow<Boolean>(false)
    val isDiscoveringDesired = _isDiscoveringDesired.asStateFlow()

    private val _isDiscovering = MutableStateFlow<Boolean>(false)
    val isDiscovering= _isDiscovering.asStateFlow()

    private val _discoveredDevices = MutableStateFlow<Set<BluetoothDevice>>(emptySet())
    val discoveredDevices = _discoveredDevices.asStateFlow()


    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                // reinitialize adapter when it turns on
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)
                    _adapterState.value = AdapterState.getByValue(state)

                    if (state == BluetoothAdapter.STATE_ON) {
                        initializeAdapter()
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun initializeAdapter(){
        val btManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        adapter = btManager.adapter

        try{
            context.unregisterReceiver(bluetoothReceiver)
        } catch (e: Exception ) {
            // Ignore if not registered
        }

        val filter = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            addAction(BluetoothDevice.ACTION_FOUND)
        }
        context.registerReceiver(bluetoothReceiver, filter)
        Log.d("KeyPadBtManager", "Adapter state: ${adapter?.state}")
        _adapterState.value = AdapterState.getByValue(adapter?.state ?: BluetoothAdapter.STATE_OFF)
        _isDiscovering.value = adapter?.isDiscovering ?: false
    }

    fun teardown() {
        // 1. Stop the Receiver
        try {
            context.unregisterReceiver(bluetoothReceiver)
        } catch (e: Exception) { }

        // 2. Close the HID Profile
        //unbindHidService()

        // 3. Reset internal flows so the UI reflects a clean slate
        _adapterState.value = AdapterState.OFF
        _isDiscovering.value = false
        //_discoveredDevices.value = emptySet()
    }

    init {
        initializeAdapter()
    }

    fun getRequiredPermissions():Array<String>{
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
                )
            }
            else -> {
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
        }
    }

    fun checkPermissions(context: Context):Map<String, Boolean>{
        val requiredPermissions = getRequiredPermissions()
        return requiredPermissions.associate({
            it to (ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED)})
    }
}