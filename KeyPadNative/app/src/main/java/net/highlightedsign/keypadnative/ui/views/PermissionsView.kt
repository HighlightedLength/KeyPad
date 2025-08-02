package net.highlightedsign.keypadnative.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import net.highlightedsign.keypadnative.ui.theme.KeyPadNativeTheme

@Composable
fun PermissionsView(
        modifier: Modifier = Modifier,
    ) {
    Column(modifier = modifier) {
        Row{
            Text(text = "Permission")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PermissionsViewPreview() {
    KeyPadNativeTheme {
        PermissionsView()
    }
}