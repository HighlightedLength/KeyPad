package net.highlightedsign.keypadkit.services

import android.os.Build
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import net.highlightedsign.keypadkit.interfaces.IBtPermissionService

class BtPermissionsService: IBtPermissionService {
    override fun getRequiredPermissions():Array<String>{
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
                )
            }
            else -> {
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
        }
    }

    override fun checkPermissions(context: Context):Map<String, Boolean>{
        val requiredPermissions = getRequiredPermissions()
        return requiredPermissions.associate({
            it to (ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED)})
    }

}