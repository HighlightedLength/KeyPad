package net.highlightedsign.keypadnative.presentation.activities

import android.content.Context
import android.content.Intent
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
                        onPermissionClick = this::onPermissionClick,
                        onBtDevClick = this::onBtDevClick,
                        onConnectClick = { testToast(this, "Connect Clicked") }
                    )
                }
            }
        }
    }

    fun onPermissionClick(){
        testToast(this, "Permission Clicked")
        val intent = Intent(this, PermissionsActivity::class.java)
        startActivity(intent)
    }

    fun onBtDevClick(){
        testToast(this, "Dev Clicked")
        val intent = Intent(this, DevBtActivity::class.java)
        startActivity(intent)
    }
}

