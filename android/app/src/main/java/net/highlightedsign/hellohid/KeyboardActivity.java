package net.highlightedsign.hellohid;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.highlightedsign.hellohid.ble.AbstractBleActivity;
import net.highlightedsign.hellohid.ble.KeyboardPeripheral;

public class KeyboardActivity extends AbstractBleActivity {
    private KeyboardPeripheral keyboard;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        setTitle(getString(R.string.ble_keyboard));

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (keyboard != null) {
                    keyboard.sendKeys(((TextView) findViewById(R.id.editText)).getText().toString());
                }
            }
        });
    }


    @Override
    protected void setupBlePeripheralProvider() {
        keyboard = new KeyboardPeripheral(this);
        keyboard.setDeviceName(getString(R.string.ble_keyboard));
        keyboard.startAdvertising();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (keyboard != null) {
            keyboard.stopAdvertising();
        }
    }
}