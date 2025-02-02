package net.highlightedsign.hellohid.ble;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import net.highlightedsign.hellohid.R;

public abstract class AbstractBleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!BleUtils.isBluetoothEnabled(this)) {
            BleUtils.enableBluetooth(this);
            return;
        }

        if (!BleUtils.isBleSupported(this) || !BleUtils.isBlePeripheralSupported(this)) {
            // display alert and exit
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.not_supported));
            alertDialog.setMessage(getString(R.string.ble_perip_not_supported));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                    (dialog, which) -> dialog.dismiss());
            alertDialog.setOnDismissListener(dialog -> finish());
            alertDialog.show();
        } else {
            setupBlePeripheralProvider();
        }
    }

    protected abstract void setupBlePeripheralProvider();

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BleUtils.REQUEST_CODE_BLUETOOTH_ENABLE) {
            if (!BleUtils.isBluetoothEnabled(this)) {
                // User selected NOT to use Bluetooth.
                // do nothing
                Toast.makeText(this, R.string.requires_bl_enabled, Toast.LENGTH_LONG).show();
                return;
            }

            if (!BleUtils.isBleSupported(this) || !BleUtils.isBlePeripheralSupported(this)) {
                // display alert and exit
                final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle(getString(R.string.not_supported));
                alertDialog.setMessage(getString(R.string.ble_perip_not_supported));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                        (dialog, which) -> dialog.dismiss());
                alertDialog.setOnDismissListener(dialog -> finish());
                alertDialog.show();
            } else {
                setupBlePeripheralProvider();
            }
        }
    }
}
