package net.highlightedsign.keypadnative

import android.app.Application
import net.highlightedsign.keypadnative.locallib.BtManager

class KeyPadNativeApplication : Application(){

    val btManager by lazy { BtManager(applicationContext) }
}