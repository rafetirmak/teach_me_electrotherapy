package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.theme.ComrafetirmakteachmeelectrotherapyTheme

import android.content.Context
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.utils.LocaleHelper
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.utils.SettingsManager

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        val settingsManager = SettingsManager(newBase)
        val context = LocaleHelper.setLocale(newBase, settingsManager.language)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComrafetirmakteachmeelectrotherapyTheme {
                ElectrotherapyApp()
            }
        }
    }
}
