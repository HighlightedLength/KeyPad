package net.highlightedsign.keypadnative.presentation.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import net.highlightedsign.keypadkit.interfaces.IBtPermissionService
import net.highlightedsign.keypadkit.services.BtPermissionsService
import net.highlightedsign.keypadnative.presentation.viewmodels.PermissionsViewModel
import net.highlightedsign.keypadnative.ui.views.PermissionsView

class PermissionsActivity: ComponentActivity() {
    private val btPermissionService : IBtPermissionService = BtPermissionsService()
    private val permissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        syncPermState()
    }

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
        syncPermState()
    }

    fun onGrantPermClick(){
        val permissions = btPermissionService.checkPermissions(this)
        for(perm in permissions) {
            Log.d("PermissionsActivity", "Permission ${perm.key}: ${perm.value}")
        }
        val allGranted = permissions.values.all { it }
        Log.d("PermissionsActivity", "All permissions granted: $allGranted")
        if (allGranted){
            onPermsGranted()
        } else {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder
                .setTitle("Permissions Request")
                .setMessage("KeyPad requires permissions to connect to your computer as a bluetooth device. Please grant them.")
                .setPositiveButton("Ok"){ _, _ -> requestPerms()}
                .setNegativeButton("No"){ _, _ -> onDenyPerms()}
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun onPermsGranted(){
        testToast(this, "All Permissions Granted")
    }

    fun onDenyPerms(){
        testToast(this, "Permissions Request Denied")
    }

    private fun requestPerms(){
        permissionsLauncher.launch(btPermissionService.getRequiredPermissions())
    }

    private fun syncPermState(){
        val updatedPermissions = btPermissionService.checkPermissions(this)
        updatedPermissions.forEach{ (permission, granted) -> viewModel.setPermission(permission, granted) }
    }
}