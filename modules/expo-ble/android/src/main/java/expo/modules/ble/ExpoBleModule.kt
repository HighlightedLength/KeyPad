package expo.modules.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.util.Log
import androidx.core.os.bundleOf
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

class ExpoBleModule : Module() {
  private var bluetoothManager: BluetoothManager? = null
  private var bluetoothAdapter: BluetoothAdapter? = null
  private var bluetoothReceiver: BroadcastReceiver? = null

  private val context
  get() = requireNotNull(appContext.reactContext)

  override fun definition() = ModuleDefinition {
    Name("ExpoBle")

    Function("setTheme") { theme: String ->
      getPreferences().edit().putString("theme", theme).commit()
    }

    Function("getTheme") {
      return@Function getPreferences().getString("theme", "system")
    }

    OnCreate {
      bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
      bluetoothAdapter = bluetoothManager?.adapter

      bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {
          when (intent?.action) {
              // reinitialize adapter when it turns on
              BluetoothAdapter.ACTION_STATE_CHANGED -> {
                  val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)
                  // _adapterState.value = AdapterState.getByValue(state)

                  if (state == BluetoothAdapter.STATE_ON) {
                    initializeAdapter()
                  }
                  else {
                    Log.d("KeyPadBtManager", state.toString())
                  }
              }
          }
        }
      }
    }
  }
  
  private fun getPreferences(): SharedPreferences {
    return context.getSharedPreferences(context.packageName + ".settings", Context.MODE_PRIVATE)
  }

  fun initializeAdapter(){
    bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    bluetoothAdapter = bluetoothManager?.adapter

    bluetoothReceiver = object : BroadcastReceiver() {
      override fun onReceive(ctx: Context?, intent: Intent?) {
        when (intent?.action) {
            // reinitialize adapter when it turns on
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)
                // _adapterState.value = AdapterState.getByValue(state)

                if (state == BluetoothAdapter.STATE_ON) {
                  initializeAdapter()
                }
                else {
                  Log.d("KeyPadBtManager", state.toString())
                }
            }
        }
      }
    }

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
    Log.d("KeyPadBtManager", "Adapter state: ${bluetoothAdapter?.state}")
    // _adapterState.value = AdapterState.getByValue(bluetoothAdapter?.state ?: BluetoothAdapter.STATE_OFF)
    // _isDiscovering.value = bluetoothAdapter?.isDiscovering ?: false
  }
}

