package net.highlightedsign.keypadnative.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.highlightedsign.keypadnative.R
import net.highlightedsign.keypadnative.ui.theme.KeyPadNativeTheme

fun rowModifier(onClick: () -> Unit = {}): Modifier {
    return Modifier
        .padding(vertical = 8.dp)
        .clickable { onClick() }
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .fillMaxWidth()
}
val elementModifier = Modifier.padding(start = 8.dp)

@Composable
fun MainView(
        modifier: Modifier = Modifier,
        onPermissionClick: () -> Unit = {},
        onConnectClick: () -> Unit = {}) {
    Column(modifier = modifier) {
        PermissionCardView(onPermissionClick)
        ConnectionCardView(onConnectClick)
    }
}

@Composable
fun PermissionCardView(onCLick: () -> Unit = {}){
    Row(
        modifier = rowModifier(onCLick),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.block_24),
            tint = MaterialTheme.colorScheme.error,
            contentDescription = "Warning")
        Text(
            text = "Permission",
            style = MaterialTheme.typography.bodyLarge,
            modifier = elementModifier)
    }
}

@Composable
fun ConnectionCardView(onCLick: () -> Unit = {}){
    Row(
        modifier = rowModifier(onCLick),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.bluetooth_searching_24dp),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "Connect")
        Text(
            text = "Connect",
            style = MaterialTheme.typography.bodyLarge,
            modifier = elementModifier)
    }
}

@Preview(showBackground = true)
@Composable
fun MainViewPreview() {
    KeyPadNativeTheme {
        MainView()
    }
}