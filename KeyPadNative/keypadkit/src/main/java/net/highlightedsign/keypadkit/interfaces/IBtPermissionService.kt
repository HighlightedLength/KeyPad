package net.highlightedsign.keypadkit.interfaces

import android.content.Context

interface IBtPermissionService {
    fun getRequiredPermissions():Array<String>
    fun checkPermissions(context: Context):Map<String, Boolean>
}