package net.highlightedsign.keypadnative.activities

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import net.highlightedsign.keypadnative.ui.theme.KeyPadNativeTheme
import net.highlightedsign.keypadnative.ui.views.MainView

fun testToast (context: Context, text: String){
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KeyPadNativeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainView(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        onPermissionClick = { testToast(this, "Permission Clicked")  },
                        onConnectClick = { testToast(this, "Connect Clicked") }
                    )
                }
            }
        }
    }
}

