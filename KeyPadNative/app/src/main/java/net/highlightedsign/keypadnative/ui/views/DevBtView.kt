package net.highlightedsign.keypadnative.ui.views

import android.bluetooth.BluetoothAdapter
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import net.highlightedsign.keypadkit.models.AdapterState
import net.highlightedsign.keypadnative.R
import net.highlightedsign.keypadnative.presentation.viewmodels.DevBtViewModel

@Composable
fun DevBtView(
    viewModel: DevBtViewModel,
    modifier: Modifier = Modifier
){
    val adapterState = viewModel.adapterState.collectAsStateWithLifecycle()
    val adapterStateText = when (adapterState.value) {
        AdapterState.ON -> "On"
        AdapterState.TURNING_ON -> "Turning On"
        AdapterState.OFF -> "Off"
        AdapterState.TURNING_OFF -> "Turning Off"
        else -> "Unknown"
    }
    Column(modifier = modifier){
        Row { Text("Dev Bt View", fontSize = 36.sp) }

        Row {
            Icon(
                painter = painterResource(id = R.drawable.power_24),
                tint = when (adapterState.value) {
                    AdapterState.ON -> Color.Green
                    AdapterState.TURNING_ON -> Color.Yellow
                    AdapterState.OFF -> Color.Gray
                    else -> Color.Red // STATE_TURNING_OFF and other values
                },
                contentDescription = "Adapter OnOff State"
            )
            Text("Adapter State: $adapterStateText")

        }
    }
}