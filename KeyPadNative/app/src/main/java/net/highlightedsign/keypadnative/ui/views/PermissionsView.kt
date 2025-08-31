package net.highlightedsign.keypadnative.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import net.highlightedsign.keypadnative.R
import net.highlightedsign.keypadnative.presentation.viewmodels.PermissionsViewModel
import net.highlightedsign.keypadnative.ui.theme.KeyPadNativeTheme

@Composable
fun PermissionsView(
    viewModel: PermissionsViewModel,
    onGrantPermClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        viewModel.permissions.forEach { (perm, isGranted) ->
            Row {
                Icon(
                    painter = painterResource(
                        id = if (isGranted.value)
                            R.drawable.bluetooth_searching_24dp else
                            R.drawable.block_24
                    ),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Granted"
                )
                Text(perm.split(".").last().replace("_", " ").lowercase())
            }
        }
        Row(rowModifier(onGrantPermClick)) {
            Text("Grant Perms")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PermissionsViewPreview() {
    KeyPadNativeTheme {
        PermissionsView(
            PermissionsViewModel(
                mapOf("Test Perm1" to true, "Test Perm2" to false)
            )
        )
    }
}