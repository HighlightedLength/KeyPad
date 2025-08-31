package net.highlightedsign.keypadnative.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import net.highlightedsign.keypadkit.interfaces.IBtPermissionService
import net.highlightedsign.keypadkit.services.BtPermissionsService
import net.highlightedsign.keypadnative.presentation.viewmodels.PermissionsViewModel
import net.highlightedsign.keypadnative.ui.views.PermissionsView

class PermissionsActivity: ComponentActivity(){
    private val btPermissionService : IBtPermissionService = BtPermissionsService()
    private lateinit var viewModel: PermissionsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = PermissionsViewModel(btPermissionService.checkPermissions(this))
        enableEdgeToEdge()
        setContent{
            net.highlightedsign.keypadnative.ui.theme.KeyPadNativeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PermissionsView(
                        viewModel = viewModel,
                        onGrantPermClick = this::onGrantPermClick,
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onResume(){
        super.onResume()
        val updatedPermissions = btPermissionService.checkPermissions(this)
        updatedPermissions.forEach{ (permission, granted) -> viewModel.setPermission(permission, granted) }
    }

    fun onGrantPermClick(){
        val key = viewModel.permissions.keys.first()
        viewModel.setPermission(key, !viewModel.permissions[key]!!.value)
        testToast(this, "Grant Clicked")
    }
}