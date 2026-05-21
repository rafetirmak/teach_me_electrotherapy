package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.theme.ComrafetirmakteachmeelectrotherapyTheme

import android.content.Context
import androidx.lifecycle.lifecycleScope
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.utils.DictionarySyncManager
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.utils.LocaleHelper
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.utils.SettingsManager
import kotlinx.coroutines.launch

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

        // Sync dictionary in background
        lifecycleScope.launch {
            DictionarySyncManager.syncDictionary(applicationContext)
        }

        setContent {
            ComrafetirmakteachmeelectrotherapyTheme {
                ElectrotherapyApp()
            }
        }
    }
}
