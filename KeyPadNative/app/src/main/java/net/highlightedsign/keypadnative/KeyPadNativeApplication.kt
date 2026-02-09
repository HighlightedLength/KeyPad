package net.highlightedsign.keypadnative

import android.app.Application
import net.highlightedsign.keypadkit.KeyPadBtManager

class KeyPadNativeApplication : Application(){

    val keyPadBtManager by lazy { KeyPadBtManager(applicationContext) }
}