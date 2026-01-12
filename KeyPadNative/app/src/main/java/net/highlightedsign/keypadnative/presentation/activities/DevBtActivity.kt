package net.highlightedsign.keypadnative.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.highlightedsign.keypadnative.KeyPadNativeApplication
import net.highlightedsign.keypadnative.R
import net.highlightedsign.keypadnative.presentation.viewmodels.DevBtViewModel
import net.highlightedsign.keypadnative.ui.theme.KeyPadNativeTheme
import net.highlightedsign.keypadnative.ui.views.DevBtView

class DevBtActivity : ComponentActivity() {
    // create view model scoped to the activity
    val viewModel: DevBtViewModel by viewModels{
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T{
                val manager = (application as KeyPadNativeApplication).btManager
                return DevBtViewModel(manager) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            KeyPadNativeTheme {
                Scaffold (Modifier.fillMaxSize()) { innerPadding ->
                    DevBtView(
                        viewModel,
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                    )
                }
            }
        }
    }
}