package net.highlightedsign.hellohid.ble;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;

public class BleUtils {

    /**
     * Check if Bluetooth LE device supported on the running environment.
     *
     * @param context the context
     * @return true if supported
     */
    public static boolean isBleSupported(@NonNull final Context context) {
        try {
            if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                return false;
            }

            final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

            final BluetoothAdapter bluetoothAdapter;
            bluetoothAdapter = bluetoothManager.getAdapter();

            if (bluetoothAdapter != null) {
                return true;
            }
        } catch (final Throwable ignored) {
            // ignore exception
        }
        return false;
    }

    /**
     * Check if Bluetooth LE Peripheral mode supported on the running environment.
     *
     * @param context the context
     * @return true if supported
     */
    @SuppressLint("NewApi")
    public static boolean isBlePeripheralSupported(@NonNull final Context context) {

        final BluetoothAdapter bluetoothAdapter =  ((BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();

        if (bluetoothAdapter == null) {
            return false;
        }

        return bluetoothAdapter.isMultipleAdvertisementSupported();
    }

    /**
     * Check if bluetooth function enabled
     *
     * @param context the context
     * @return true if bluetooth enabled
     */
    public static boolean isBluetoothEnabled(@NonNull final Context context) {
        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

        if (bluetoothManager == null) {
            return false;
        }

        final BluetoothAdapter bluetoothAdapter;
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null) {
            return false;
        }

        return bluetoothAdapter.isEnabled();
    }

    /**
     * Request code for bluetooth enabling
     */
    public static final int REQUEST_CODE_BLUETOOTH_ENABLE = 0xb1e;

    /**
     * Enables bluetooth function.<br />
     * the Activity may implement the `onActivityResult` method with the request code `REQUEST_CODE_BLUETOOTH_ENABLE`.
     *
     * @param activity the activity
     */
    public static void enableBluetooth(@NonNull final Activity activity) {
        activity.startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_CODE_BLUETOOTH_ENABLE);
    }
}
